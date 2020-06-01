package com.sunjet.utils.macros;

import com.sunjet.utils.common.JsonHelper;
import com.sunjet.utils.activiti.CustomComment;
import org.zkoss.zk.ui.HtmlMacroComponent;

import java.io.IOException;

/**
 * Created by lhj on 16/10/20.
 */
//@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver)
public class CommentHandler extends HtmlMacroComponent {
    public CustomComment getBeanFromJson(String json) throws IOException {
        CustomComment comment = JsonHelper.json2Bean(json, CustomComment.class);
        return comment;
    }
}
