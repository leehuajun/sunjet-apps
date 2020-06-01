package com.sunjet.vm.flow;

import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.io.IOException;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class HandleFormVM extends FormBaseVM {
    @WireVariable
    private ProcessService processService;
    private Task task;
    private List<String> outcomes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private Map<String, String> users = new HashMap<>();
    private String comment = "";
//    private String key = "";

    public List<Comment> getComments() {
        return comments;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    @Init(superclass = true)
    public void init() throws IOException {
        outcomes = (List<String>) Executions.getCurrent().getArg().get("outcomes");
        Collections.sort(outcomes);//排序按钮
        comments = (List<Comment>) Executions.getCurrent().getArg().get("comments");
        users = (Map<String, String>) Executions.getCurrent().getArg().get("users");
//        key = (String) Executions.getCurrent().getArg().get("key")
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void commit(@BindingParam("outcome") String outcome) {
        if (StringUtils.isBlank(this.getComment().trim())) {
            ZkUtils.showInformation("请填写审批意见", "提示");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("outcome", outcome);
        map.put("comment", StringUtils.isBlank(this.comment) ? "同意" : this.comment);
//        map.put("key",key);
//        if(StringUtils.equals(outcome,"发起流程")){  // 新发起流程
//            BindUtils.postGlobalCommand(null,null, GlobalCommandValues.START_PROCESS,map);
//        }else{  // 审批节点，非新发起流程
//            BindUtils.postGlobalCommand(null,null, GlobalCommandValues.COMMIT_TASK,map);
//        }


        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.COMMIT_TASK, map);
//                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                } else {
                    return;
                }
            }
        });
    }
}
