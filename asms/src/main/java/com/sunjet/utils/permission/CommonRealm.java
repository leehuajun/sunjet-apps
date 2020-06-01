package com.sunjet.utils.permission;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.service.admin.MenuService;
import com.sunjet.service.admin.PermissionService;
import com.sunjet.service.admin.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 自定义 Realm
 * <p>
 * Created by lhj on 15/10/30.
 */
public class CommonRealm extends AuthorizingRealm {

    //注入service
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;

//	@PostConstruct
//	public void initCredentialsMatcher() {
//		//该句作用是重写shiro的密码验证，让shiro用我自己的验证
//		setCredentialsMatcher(new CustomCredentialsMatcher());
//
//	}

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    //realm的认证方法，从数据库查询用户信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        // token 是用户输入的用户名和密码
        // 第一步从 token 中取出用户身份/凭证,即用户名/密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String logId = usernamePasswordToken.getUsername();
        System.out.println(logId);
        // 第二步：根据用户输入的 logId 从数据库查询
        UserEntity userEntity = null;
        try {
            userEntity = userService.findOneByLogId(logId);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // 认证用户名是否正确，错误返回 null
        if (userEntity == null) {
            throw new UnknownAccountException(); //如果用户名错误
        }
        // 从数据库查询到密码
        String password = userEntity.getPassword();
        // 盐
        String salt = userEntity.getSalt();

        //activeUser就是用户身份信息
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserId(userEntity.getObjId());
        activeUser.setLogId(userEntity.getLogId());
        activeUser.setUsername(userEntity.getName());
        activeUser.setDealer(userEntity.getDealer());
        activeUser.setAgency(userEntity.getAgency());
        activeUser.setUserType(userEntity.getUserType());
        activeUser.setPhone(userEntity.getPhone());
//        activeUser.setOrgEntity(userEntity.getOrgEntity());
//        System.out.println(currentUser);
        //..


        //将 currentUser 设置 simpleAuthenticationInfo
        //如果身份认证验证成功，返回一个认证信息 AuthenticationInfo
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                activeUser, password, ByteSource.Util.bytes(salt), this.getName());
        return simpleAuthenticationInfo;
    }


    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        //从 principals获取主身份信息
        //将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
        ActiveUser activeUser = (ActiveUser) principals.getPrimaryPrincipal();

        List<String> permissionList = permissionService.findAllByUserId(activeUser.getUserId());
        activeUser.setPermissions(permissionList);
        List<MenuEntity> menus = menuService.findAllByUserId(activeUser.getUserId());

        Set<MenuEntity> userMenus = new HashSet<>();
        for (MenuEntity me : menus) {
            if (permissionList.contains(me.getPermissionCode())) {
                userMenus.add(me);
//                userMenus.add(me.getParent());
                chkParetMenu(userMenus, me);
            }
        }
        List<MenuEntity> menuList = new ArrayList<>(userMenus);
        Collections.sort(menuList, new Comparator<MenuEntity>() {
            /*
             * int compare(MenuEntity o1, MenuEntity o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(MenuEntity o1, MenuEntity o2) {

                //按照菜单序号进行升序排列
                if (o1.getSeq() > o2.getSeq()) {
                    return 1;
                }
                if (o1.getSeq() == o2.getSeq()) {
                    return 0;
                }
                return -1;
            }
        });
        activeUser.setMenus(menuList);
        System.out.println(activeUser);

//            currentUser.setPermissions(permissionList);

//        List<String> permissions = new ArrayList<String>();
//        if (permissionList != null) {
//            for (PermissionEntity permissionEntity : permissionList) {
//                //将数据库中的权限标签 符放入集合
//                permissions.add(permissionEntity.getPermissionCode());
//            }
//        }
        //单独定一个集合对象

//		List<String> permissions = new ArrayList<String>();
//		permissions.add("user:create");//用户的创建
//		permissions.add("item:query");//商品查询权限
//		permissions.add("item:add");//商品添加权限
//		permissions.add("item:edit");//商品修改权限


        //查到权限数据，返回授权信息(要包括 上边的permissions)
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        simpleAuthorizationInfo.addStringPermissions(permissionList);

        return simpleAuthorizationInfo;
    }

    //清除缓存
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

    private void chkParetMenu(Set<MenuEntity> menus, MenuEntity menu) {
        if (menu.getParent() != null) {
            menus.add(menu.getParent());
            chkParetMenu(menus, menu.getParent());
        }
    }
}
