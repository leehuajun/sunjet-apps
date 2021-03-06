.z-tab {
    display: block;
    border: 1px solid #e3e3e3;
    /*border-top: 1px solid #5687a8;*/
    padding: 0px;
    line-height: 32px;
    background: rgb(78, 116, 149);
    color: rgb(255, 255, 255);
    text-align: center;
    position: relative;
    cursor: pointer;
    float: left;
    box-sizing: border-box;
    /*标签的形状,弧形设置border-radius:12px 3px 0 0*/
    border-radius: 0 0 0 0;
}

.z-tab-selected {
    background: #fff;
    font-weight: 700;
    color: #5687a8;
}

.z-tab-button {
    box-sizing: border-box;
    box-sizing: content-box;
    color: #5687a8;
}

.z-tabbox-icon {
    font-size: 12px;
    color: #fff;
    display: none;
    min-height: 28px;
    border: 1px solid #e3e3e3;
    line-height: 28px;
    background: #5687a8;
    text-align: center;
    position: absolute;
    top: 0;
    cursor: pointer;
    z-index: 25;
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none
}

.z-tab-selected .z-tab-content:hover {
    text-decoration: none
}

.z-tab-text {
    color: rgb(255, 255, 255);
}

.z-tab:focus {
    /*background: none;*/
    background: rgb(78, 116, 149);
    color: rgb(255, 255, 255);
}

.rcaption.z-a {
    font-size: 17px;
    cursor: default;
}

.rcaption.z-a > [class*="z-icon-"] {
    margin-right: 8px;
}

.rtab.z-tabbox {
    position: absolute;
    top: 0px;
    /*#tab 背景颜色*/
    /*background-color: rgb(249,249,249);*/
    background-color: rgb(227, 227, 227);
}

.rtab.z-tabbox .z-tabs-content {
    border-bottom-color: rgb(227, 227, 227);
    padding: 0 1px 0 0;
}

.rtab.z-tabbox .z-tab {
    /*border-top-left-radius: 10px;*/
    /*border-top-right-radius: 2px;*/
    float: left;
    border-width: 1px 1px 0 1px;
    border-color: rgb(101, 139, 171);
    color: rgb(255, 255, 255);
    padding-top: 0;
    font-family: 'Open Sans';
    /*background: rgb(147, 182, 206);*/
    background: rgb(101, 139, 171);
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    /*background-color: rgb(255, 255, 255);*/
    /*background-color: transparent;*/
    /*background-color: rgb(226, 226, 226);*/
}

.rtab.z-tabbox .z-tab .z-tab-icon {
    /*border-width: 1px 1px 1px 1px;*/
    /*border-color: rgb(101,139,171);*/
    /*margin-top: -2px;*/
    /*border: 1px solid rgb(101,139,171);*/

    width: 15px;
    height: 15px;
    color: rgb(255, 255, 255);
    /*padding: 2px 2px 2px 2px;*/
}

.rtab.z-tabbox .z-tab:focus {
    /*background: none;*/
    color: rgb(44, 64, 82);
    background: rgb(78, 116, 149);
    border-top: 1px solid rgb(101, 139, 171);
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    /*background-color: rgb(255, 255, 255);*/
    /*background-color: transparent;*/
}

.rtab.z-tabbox .z-tab:active {
    background: none;
    color: #000;
    border-left: 1px solid rgb(227, 227, 227);
    border-right: 1px solid rgb(227, 227, 227);
    border-top: 1px solid rgb(101, 139, 171);
    /*border-top-color: rgb(101,139,171);*/
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    /*background-color: rgb(255, 255, 255);*/
    /*background-color: transparent;*/
}

.rtab.z-tabbox .z-tab:active .z-tab-text {
    color: rgb(16, 110, 171);
    font-weight: 700;
}

.rtab.z-tabbox .z-tab:active .z-tab-icon {
    color: rgb(16, 110, 171);
    font-weight: 700;
}

.rtab.z-tabbox .z-tab-selected {
    /*border-style: solid;*/
    font-weight: 700;
    background-color: rgb(255, 255, 255);
    /*border-left:1px solid rgb(227,227,227);*/
    border-left: 1px solid rgb(255, 255, 255);
    border-right: 1px solid rgb(255, 255, 255);
    /*border-right:1px solid rgb(227,227,227);*/
    border-top: 1px solid rgb(101, 139, 171);
    /*border-top-color: rgb(101,139,171);*/
}

.rtab.z-tabbox .z-tab-selected .z-tab-text {
    font-weight: 700;
    background-color: rgb(255, 255, 255);
    color: rgb(16, 110, 171);
}

.rtab.z-tabbox .z-tab-selected .z-tab-icon {
    font-weight: 700;
    background-color: rgb(255, 255, 255);
    color: rgb(16, 110, 171);
}

.rtab.z-tabbox .z-tab .z-tab-icon:hover {
    /*border: 1px solid rgb(101,139,171);*/
    background-color: rgb(255, 255, 255);
    color: rgb(16, 110, 171);
}

.rtab.z-tabbox .z-tab-selected .z-tab-icon:hover {
    /*font-weight: 700;*/
    /*background-color: rgb(255, 255, 255);*/
    background-color: rgb(16, 110, 171);
    color: rgb(255, 255, 255);
}

.rtab.z-tabbox .z-tabpanel {
    border: 0;
    padding: 5px 3px;
    background-color: rgb(255, 255, 255);
}

.rtab.z-tabbox .rcaption {
    display: block;
    width: auto;
    height: 18px;
    margin: 0px 0px;
    font-size: 17px;
    line-height: normal;
}

.rtab.z-tabbox .z-tab:first-child.z-tab-selected + .z-tab {
    /*${ t:boxShadow('none') };*/
}

.ntab.z-tabbox {
    position: absolute;
    top: 0px;
    /*#tab 背景颜色*/
    /*background-color: rgb(249,249,249);*/
    background-color: rgb(255, 255, 255);
}

.ntab.z-tabbox .z-tab .z-tab-text {
    color: rgb(101, 139, 171);
}

.ntab.z-tabbox .z-tab {
    /*border-top-left-radius: 10px;*/
    /*border-top-right-radius: 2px;*/
    float: left;
    border-width: 1px 1px 0 1px;
    border-color: rgb(255, 255, 255);
    color: rgb(101, 139, 171);
    padding-top: 0;
    font-family: 'Open Sans';
    /*background: rgb(147, 182, 206);*/
    background: rgb(255, 255, 255);
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    /*background-color: rgb(255, 255, 255);*/
    /*background-color: transparent;*/
    /*background-color: rgb(226, 226, 226);*/
}

.ntab.z-tabbox .z-tab-selected {
    /*border-style: solid;*/
    font-weight: 700;
    background-color: rgb(255, 255, 255);
    /*border-left:1px solid rgb(227,227,227);*/
    border-left: 1px solid rgb(227, 227, 227);
    border-right: 1px solid rgb(227, 227, 227);
    /*border-right:1px solid rgb(227,227,227);*/
    border-top: 1px solid rgb(101, 139, 171);
    /*border-top-color: rgb(101,139,171);*/
}

.ntab.z-tabbox .z-tabpanel {
    border: 0;
    padding: 0 0 0 0;

}

.rtab.z-tabbox .z-tabpanel {
    border: 0;
    padding: 5px 3px;
    background-color: rgb(255, 255, 255);
}