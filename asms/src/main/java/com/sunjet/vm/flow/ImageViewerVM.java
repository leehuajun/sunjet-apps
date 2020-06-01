package com.sunjet.vm.flow;

import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.UUIDHelper;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ImageViewerVM extends FormBaseVM {
    @WireVariable
    private ProcessService processService;

    //    private String imagePath;
    private BufferedImage flowBufferedImage;
    private Map<String, Object> coording;
    private Boolean show = true;

    //    private String url;
//
//    public String getUrl() {
//        return url;
//    }
//    public String getImagePath() {
//        return imagePath;
//    }
    public Map<String, Object> getCoording() {
        return coording;
    }

    public Boolean getShow() {
        return show;
    }

    public BufferedImage getFlowBufferedImage() {
        return flowBufferedImage;
    }

    //    @Init(superclass = true)
//    public void init() throws IOException {
//        Task task = (Task) Executions.getCurrent().getArg().get("task");
//
//        url = "/diagram-viewer/index.html?processInstanceId=" + task.getProcessInstanceId()
//                + "&processDefinitionId=" + task.getProcessDefinitionId();
//    }
    @Init(superclass = true)
    public void init() throws IOException {
        Task task = (Task) Executions.getCurrent().getArg().get("task");

        ProcessDefinition processDefinition = (ProcessDefinition) Executions.getCurrent().getArg().get("processDefinition");
        if (processDefinition == null && task == null) {
            ZkUtils.showError("流程定义实体和任务实体不能同时为空，请与管理员联系！", "系统错误");
            return;
        }
        if (processDefinition == null) {
            processDefinition = processService.findProcessDefinition(task.getProcessDefinitionId());
        }
        /**将生成图片放到文件夹下*/
//        String deploymentId = "12501";
        // 获取图片资源名称
//        String tmpPath = "/image-caches/"+processDefinition.getId().replace(":","-")+".png";
////        String tmpPath = "/image-caches/"+ UUIDHelper.newUuid() +".png";
//        String imageFile = Executions.getCurrent().getDesktop().getWebApp().getRealPath(tmpPath);
//        File file = new File(imageFile);
//        if(!file.exists()){
//            // 获取图片的输入流
//            InputStream inputStream = processService.findImageInputStream(processDefinition);
//
//            // 将图片生成本地目录下
////        File file = new File("/Users/lhj/" + resourceName);
//            FileUtils.copyInputStreamToFile(inputStream, file);
////            this.imagePath = imageFile;
//        }

//        this.imagePath = tmpPath;

        InputStream inputStream = processService.findImageInputStream(processDefinition);
        this.flowBufferedImage = ImageIO.read(inputStream);

        if (task != null) {
            Map<String, Object> tmpCoording = processService.findCoordingByTask(task.getId());
//            添加红色的边框为: height  width  x+3  y+31
//            tmpCoording.replace("x",(Integer)tmpCoording.get("x") + 3);
//            tmpCoording.replace("y",(Integer)tmpCoording.get("y") + 31);
            coording = tmpCoording;
            System.out.println("x:" + coording.get("x"));
            System.out.println("y:" + coording.get("y"));
            System.out.println("width:" + coording.get("width"));
            System.out.println("height:" + coording.get("height"));
        } else {
            coording = new HashMap<>();
            coording.put("x", 0);
            coording.put("y", 0);
            coording.put("width", 0);
            coording.put("height", 0);
            show = false;
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


}
