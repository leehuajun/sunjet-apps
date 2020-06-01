package com.sunjet.vm;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ModalWindowVM {

//	@WireVariable
//	private UserServiceImpl userService;


    @Wire("#modalWindow")
    private Window modalWindow;

    @Init
    public void init() {
//		System.out.println(modalWindow==null?"modalWindow is null":"modalWindow is not null");
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
//        System.out.println(myComponent==null?"myComponent is null":"myComponent is not null");
//        winModifyPassword.setAttribute("visible",false);
//        System.out.println(leftNav==null?"leftNav is null":"leftNav is not null");

//        ((Div)myComponent).setStyle("padding:0px");

//        Div parent = (Div)myDiv.getParent();
//        System.out.println(parent.toString());
//        parent.setStyle("padding:0px");
    }

    @Command
    public void closeModalWindow() {
        modalWindow.detach();
        BindUtils.postGlobalCommand(null, null, "myGlobalCommand", null);
    }
}
