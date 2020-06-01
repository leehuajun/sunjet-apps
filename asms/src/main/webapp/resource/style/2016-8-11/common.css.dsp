/*<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>*/
/*<%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %>*/
/*<%@ taglib uri="http://www.zkoss.org/dsp/web/theme" prefix="t" %>*/

body {
    background: rgb(240, 240, 240) !important;
    color: #3d3d3d !important;
}

a:hover, a:visited, a:active, a:focus {
    text-decoration: none;
}

.z-div{
    background-color: #FFFFFF;
    margin:0px 0px;
    border:none
}

.z-window-header {
    background-color: RGB(78, 116, 149);
    color: whitesmoke;
    /*color : mediumvioletred;*/
    font-size: 13px;
    padding: 3px 0px 5px 3px;
}

.z-window-content {
    padding: 0px;
}

/*.z-vlayout {*/
/*padding:0px;*/
/*}*/

.z-vlayout .z-vlayout-inner {
    padding-bottom: 0px;
}

.z-select .z-option {
    height: 22px;
}

.z-a,
.z-button,
.z-label,
.z-menu-text,
.z-menuitem-text,
.z-nav-text,
.z-navitem-text {
    /*font-family: 'Open Sans';*/
    /*font-size: 13px;*/
    font-family: 'Open Sans';
    font-size: 12px;
    /*color: #707070;*/
}

.z-a > [class*="icon-"] {
    margin-right: 5px;
}

.z-a[disabled] {
    color: #669FC7;
}

.link.z-a:hover {
    text-decoration: none;
}

.z-caption {
    min-height: 38px;
    padding-left: 3px;
    font-size: 18px;
}

.z-caption.small {
    min-height: 31px;
    padding-left: 10px;
}

.z-caption.small .z-caption-content {
    line-height: 30px;
    font-size: 15px;
}

.z-caption-content {
    padding: 0;
    font-family: 'Open Sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;
    font-weight: lighter;
    line-height: 32px;
}

.z-caption-content [class*="z-icon-"] {
    margin-right: 5px;
}

.z-caption .z-button {
    margin: 3px 8px;
}

.z-caption .z-button:after {
    display: inline-block;
    width: 14px;
    height: 14px;
    font-family: FontAwesome;
    font-weight: normal;
    font-style: normal;
    text-decoration: inherit;
    -webkit-font-smoothing: antialiased;
    *margin-right: .3em;
    font-size: 12px;
    /*color: #ffffff;*/
    content: "\f107";
    margin-left: 2px;
}

.z-separator-horizontal-bar {
    position: relative;
    height: 64px;
    background-image: none;
}

.z-separator-horizontal-bar:before {
    display: block;
    width: 100%;
    height: 0;
    content: "";
    border-width: 0;
    border-top: 1px dotted #E3E3E3;
    position: absolute;
    top: 32px;
}

.double16.z-separator-horizontal-bar {
    height: 16px;
}

.double16.z-separator-horizontal-bar:before {
    height: 3px;
    border-bottom: 1px dotted #E3E3E3;
    top: 6px;
}

.solid.z-separator-horizontal-bar:before {
    border-style: solid;
}

/*.z-scrollbar.z-scrollbar-vertical {*/
/*width: 7px;*/
/*}*/

/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-up,*/
/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-down,*/
/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-icon,*/
/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-rail {*/
/*display: none;*/
/*}*/

/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-wrapper {*/
/*top: 0;*/
/*}*/

/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-wrapper .z-scrollbar-indicator {*/
/*background-image: none;*/
/*!*${ t:borderRadius('0') };*!*/
/*border-radius: 0px;*/
/*background-color: #999;*/
/*border: 0;*/
/*}*/

/*.z-scrollbar.z-scrollbar-vertical .z-scrollbar-wrapper .z-scrollbar-rail {*/
/*background-color: transparent;*/
/*}*/

.z-drop-ghost .z-drop-content {
    width: auto;
    height: auto;
    font-family: 'Open Sans';
    font-size: 12px;
    border: 1px solid #CCC;
}

.blue {
    color: #478FCA !important;
}

.green {
    color: #69AA46 !important;
}

.grey {
    color: #777 !important;
}

.purple {
    color: #A069C3 !important;
}

.red {
    color: #DD5A43 !important;
}

.orange {
    color: #FF892A !important;
}

.lighter {
    font-weight: lighter;
}

.sign > [class*="z-icon-"] {
    margin: 0 8px;
}

.sign-group div + div {
    border-left: 1px solid #E3E3E3;
}

.icon-2x {
    font-size: 2em;
}

.icon-mr8 {
    margin-right: 8px;
}

.bigger-130 {
    font-size: 130%;
}

.bigger-110 {
    font-size: 110%;
}

.bigger-120 {
    font-size: 120%;
}

.label {
    /*${ t:borderRadius('0') };*/
    text-shadow: none;
    font-weight: normal;
    display: inline-block;
    background-color: #abbac3 !important;
}

.label.arrowed,
.label.arrowed-in {
    position: relative;
    z-index: 1;
}

.label.arrowed:before,
.label.arrowed-in:before {
    display: inline-block;
    content: "";
    position: absolute;
    top: 0;
    z-index: -1;
    border: 1px solid transparent;
    border-right-color: #abbac3;
}

.label.arrowed-in:before {
    border-color: #abbac3;
    border-left-color: transparent !important;
}

.label.arrowed-right,
.label.arrowed-in-right {
    position: relative;
    z-index: 1;
}

.label.arrowed-right:after,
.label.arrowed-in-right:after {
    display: inline-block;
    content: "";
    position: absolute;
    top: 0;
    z-index: -1;
    border: 1px solid transparent;
    border-left-color: #abbac3;
}

.label.arrowed-in-right:after {
    border-color: #abbac3;
    border-right-color: transparent !important;
}

.label-info.arrowed:before {
    border-right-color: #3a87ad;
}

.label-info.arrowed-in:before {
    border-color: #3a87ad;
}

.label-info.arrowed-right:after {
    border-left-color: #3a87ad;
}

.label-info.arrowed-in-right:after {
    border-color: #3a87ad;
}

.label-primary.arrowed:before {
    border-right-color: #428bca;
}

.label-primary.arrowed-in:before {
    border-color: #428bca;
}

.label-primary.arrowed-right:after {
    border-left-color: #428bca;
}

.label-primary.arrowed-in-right:after {
    border-color: #428bca;
}

.label-success.arrowed:before {
    border-right-color: #82af6f;
}

.label-success.arrowed-in:before {
    border-color: #82af6f;
}

.label-success.arrowed-right:after {
    border-left-color: #82af6f;
}

.label-success.arrowed-in-right:after {
    border-color: #82af6f;
}

.label-warning.arrowed:before {
    border-right-color: #f89406;
}

.label-warning.arrowed-in:before {
    border-color: #f89406;
}

.label-warning.arrowed-right:after {
    border-left-color: #f89406;
}

.label-warning.arrowed-in-right:after {
    border-color: #f89406;
}

.label-important.arrowed:before {
    border-right-color: #d15b47;
}

.label-important.arrowed-in:before {
    border-color: #d15b47;
}

.label-important.arrowed-right:after {
    border-left-color: #d15b47;
}

.label-important.arrowed-in-right:after {
    border-color: #d15b47;
}

.label-danger.arrowed:before {
    border-right-color: #d15b47;
}

.label-danger.arrowed-in:before {
    border-color: #d15b47;
}

.label-danger.arrowed-right:after {
    border-left-color: #d15b47;
}

.label-danger.arrowed-in-right:after {
    border-color: #d15b47;
}

.label-inverse.arrowed:before {
    border-right-color: #333333;
}

.label-inverse.arrowed-in:before {
    border-color: #333333;
}

.label-inverse.arrowed-right:after {
    border-left-color: #333333;
}

.label-inverse.arrowed-in-right:after {
    border-color: #333333;
}

.label-pink.arrowed:before {
    border-right-color: #d6487e;
}

.label-pink.arrowed-in:before {
    border-color: #d6487e;
}

.label-pink.arrowed-right:after {
    border-left-color: #d6487e;
}

.label-pink.arrowed-in-right:after {
    border-color: #d6487e;
}

.label-purple.arrowed:before {
    border-right-color: #9585bf;
}

.label-purple.arrowed-in:before {
    border-color: #9585bf;
}

.label-purple.arrowed-right:after {
    border-left-color: #9585bf;
}

.label-purple.arrowed-in-right:after {
    border-color: #9585bf;
}

.label-yellow.arrowed:before {
    border-right-color: #fee188;
}

.label-yellow.arrowed-in:before {
    border-color: #fee188;
}

.label-yellow.arrowed-right:after {
    border-left-color: #fee188;
}

.label-yellow.arrowed-in-right:after {
    border-color: #fee188;
}

.label-light.arrowed:before {
    border-right-color: #e7e7e7;
}

.label-light.arrowed-in:before {
    border-color: #e7e7e7;
}

.label-light.arrowed-right:after {
    border-left-color: #e7e7e7;
}

.label-light.arrowed-in-right:after {
    border-color: #e7e7e7;
}

.label-grey.arrowed:before {
    border-right-color: #a0a0a0;
}

.label-grey.arrowed-in:before {
    border-color: #a0a0a0;
}

.label-grey.arrowed-right:after {
    border-left-color: #a0a0a0;
}

.label-grey.arrowed-in-right:after {
    border-color: #a0a0a0;
}

.label {
    font-size: 12px;
    line-height: 1.15;
    height: 20px;
}

.label.arrowed {
    margin-left: 5px;
}

.label.arrowed:before {
    left: -10px;
    border-width: 10px 5px;
}

.label.arrowed-in {
    margin-left: 5px;
}

.label.arrowed-in:before {
    left: -5px;
    border-width: 10px 5px;
}

.label.arrowed-right {
    margin-right: 5px;
}

.label.arrowed-right:after {
    right: -10px;
    border-width: 10px 5px;
}

.label.arrowed-in-right {
    margin-right: 5px;
}

.label.arrowed-in-right:after {
    right: -5px;
    border-width: 10px 5px;
}

.label-sm {
    padding: 0.2em 0.4em 0.3em;
    font-size: 11px;
    line-height: 1;
    height: 18px;
}

.label-sm.arrowed {
    margin-left: 4px;
}

.label-sm.arrowed:before {
    left: -8px;
    border-width: 9px 4px;
}

.label-sm.arrowed-in {
    margin-left: 4px;
}

.label-sm.arrowed-in:before {
    left: -4px;
    border-width: 9px 4px;
}

.label-sm.arrowed-right {
    margin-right: 4px;
}

.label-sm.arrowed-right:after {
    right: -8px;
    border-width: 9px 4px;
}

.label-sm.arrowed-in-right {
    margin-right: 4px;
}

.label-sm.arrowed-in-right:after {
    right: -4px;
    border-width: 9px 4px;
}

.label > span,
.label > [class*="z-icon-"] {
    line-height: 1;
    vertical-align: bottom;
}

.infobox-container {
    text-align: center;
    font-size: 0;
}

.infobox {
    display: inline-block;
    width: 210px;
    height: 66px;
    /*${ t:boxShadow('none') };*/
    /*${ t:borderRadius('0') };*/
    color: #555;
    background-color: #FFF;
    margin: -1px 0 0 -1px;
    padding: 8px 3px 6px 9px;
    border: 1px dotted;
    border-color: #D8D8D8 !important;
    vertical-align: middle;
    text-align: left;
    position: relative;
}

.infobox > .infobox-icon {
    display: inline-block;
    vertical-align: top;
    width: 44px;
}

.infobox > .infobox-icon > [class*="z-icon-"] {
    display: inline-block;
    height: 42px;
    margin: 0;
    padding: 1px 1px 0 2px;
    background-color: transparent;
    border: none;
    text-align: center;
    position: relative;
    /*${ t:borderRadius('100%') };*/
    /*${ t:boxShadow('1px 1px 0 rgba(0,0,0,0.2)') };*/
}

.infobox > .infobox-icon > [class*="z-icon-"]:before {
    color: #FFF;
    font-size: 24px;
    color: rgba(255, 255, 255, 0.9);
    display: block;
    padding-top: 2px;
    width: 40px;
    text-align: center;
    /*${ t:borderRadius('100%') };*/
    background-color: rgba(255, 255, 255, 0.2);
    text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.14);
}

.infobox .infobox-content {
    color: #555;
}

.infobox .infobox-content:first-child {
    font-weight: bold;
}

.infobox > .infobox-data {
    display: inline-block;
    border: none;
    border-top-width: 0;
    font-size: 12px;
    text-align: left;
    line-height: 21px;
    min-width: 130px;
    padding-left: 8px;
    position: relative;
    top: 0;
}

.infobox > .infobox-data > .infobox-data-number {
    display: block;
    font-size: 22px;
    margin: 2px 0 4px;
    position: relative;
    text-shadow: 1px 1px 0 rgba(0, 0, 0, 0.15);
}

.infobox > .infobox-data > .infobox-text {
    display: block;
    font-size: 16px;
    margin: 2px 0 4px;
    position: relative;
    text-shadow: none;
}

.infobox.no-border {
    border: none !important;
}

.infobox-purple {
    color: #6f3cc4;
    border-color: #6f3cc4;
}

.infobox-purple > .infobox-icon > [class*="z-icon-"] {
    background-color: #6f3cc4;
}

.infobox-purple.infobox-dark {
    background-color: #6f3cc4;
    border-color: #6f3cc4;
}

.infobox-pink {
    color: #cb6fd7;
    border-color: #cb6fd7;
}

.infobox-pink > .infobox-icon > [class*="z-icon-"] {
    background-color: #cb6fd7;
}

.infobox-pink.infobox-dark {
    background-color: #cb6fd7;
    border-color: #cb6fd7;
}

.infobox-blue {
    color: #6fb3e0;
    border-color: #6fb3e0;
}

.infobox-blue > .infobox-icon > [class*="z-icon-"] {
    background-color: #6fb3e0;
}

.infobox-blue.infobox-dark {
    background-color: #6fb3e0;
    border-color: #6fb3e0;
}

.infobox-blue2 {
    color: #3983c2;
    border-color: #3983c2;
}

.infobox-blue2 > .infobox-icon > [class*="z-icon-"] {
    background-color: #3983c2;
}

.infobox-blue2.infobox-dark {
    background-color: #3983c2;
    border-color: #3983c2;
}

.infobox-red {
    color: #d53f40;
    border-color: #d53f40;
}

.infobox-red > .infobox-icon > [class*="z-icon-"] {
    background-color: #d53f40;
}

.infobox-red.infobox-dark {
    background-color: #d53f40;
    border-color: #d53f40;
}

.infobox-orange {
    color: #f79263;
    border-color: #f79263;
}

.infobox-orange > .infobox-icon > [class*="z-icon-"] {
    background-color: #f79263;
}

.infobox-orange.infobox-dark {
    background-color: #f79263;
    border-color: #f79263;
}

.infobox-green {
    color: #9abc32;
    border-color: #9abc32;
}

.infobox-green > .infobox-icon > [class*="z-icon-"] {
    background-color: #9abc32;
}

.infobox-green.infobox-dark {
    background-color: #9abc32;
    border-color: #9abc32;
}

.infobox-grey {
    color: #999999;
    border-color: #999999;
}

.infobox-grey > .infobox-icon > [class*="z-icon-"] {
    background-color: #999999;
}

.infobox-grey.infobox-dark {
    background-color: #999999;
    border-color: #999999;
}

.infobox-dark {
    margin: 1px 1px 0 0;
    border-color: transparent !important;
    border: none;
    color: #FFF;
    padding: 4px;
}

.infobox-dark > .infobox-icon > [class*="z-icon-"],
.infobox-dark > .infobox-icon > [class*="z-icon-"]:before {
    background-color: transparent;
    /*${ t:boxShadow('none') };*/
    text-shadow: none;
    /*${ t:borderRadius('0') };*/
    font-size: 30px;
}

.infobox-dark > .infobox-icon > [class*="z-icon-"]:before {
    opacity: 1;
    filter: alpha(opacity=100);
}

.infobox-dark .infobox-content {
    color: #FFF;
}

.infobox > .infobox-progress {
    padding-top: 0;
    display: inline-block;
    vertical-align: top;
    width: 44px;
}

.infobox > .infobox-chart {
    padding-top: 0;
    display: inline-block;
    vertical-align: text-bottom;
    width: 44px;
    text-align: center;
}

.infobox > .infobox-chart > .sparkline {
    font-size: 24px;
}

.infobox > .infobox-chart canvas {
    vertical-align: middle !important;
}

.infobox > .stat {
    display: inline-block;
    position: absolute;
    right: 20px;
    top: 11px;
    text-shadow: none;
    font-size: 12px;
    color: #ABBAC3;
    font-weight: bold;
    padding-right: 18px;
    padding-top: 3px;
}

.infobox > .stat:before {
    display: inline-block;
    width: 8px;
    height: 11px;
    content: "";
    background-color: #ABBAC3;
    position: absolute;
    right: 4px;
    top: 7px;
}

.infobox > .stat:after {
    display: inline-block;
    content: "";
    position: absolute;
    right: 1px;
    top: -8px;
    border: 12px solid transparent;
    border-width: 8px 7px;
    border-bottom-color: #ABBAC3;
}

.infobox > .stat.stat-success {
    color: #77C646;
}

.infobox > .stat.stat-success:before {
    background-color: #77C646;
}

.infobox > .stat.stat-success:after {
    border-bottom-color: #77C646;
}

.infobox > .stat.stat-important {
    color: #E4564F;
}

.infobox > .stat.stat-important:before {
    background-color: #E4564F;
    top: 3px;
}

.infobox > .stat.stat-important:after {
    border-top-color: #E4564F;
    border-bottom-color: transparent;
    bottom: -6px;
    top: auto;
}

.infobox.infobox-dark > .stat {
    color: #FFF;
}

.infobox.infobox-dark > .stat:before {
    background-color: #E1E5E8;
}

.infobox.infobox-dark > .stat:after {
    border-bottom-color: #E1E5E8;
}

.infobox.infobox-dark > .stat.stat-success {
    color: #FFF;
}

.infobox.infobox-dark > .stat.stat-success:before {
    background-color: #D0E29E;
}

.infobox.infobox-dark > .stat.stat-success:after {
    border-bottom-color: #D0E29E;
}

.infobox.infobox-dark > .stat.stat-important {
    color: #FFF;
}

.infobox.infobox-dark > .stat.stat-important:before {
    background-color: #FF8482;
    top: 3px;
}

.infobox.infobox-dark > .stat.stat-important:after {
    border-top-color: #FF8482;
    border-bottom-color: transparent;
    bottom: -6px;
    top: auto;
}

.infobox > .badge {
    position: absolute;
    right: 20px;
    top: 11px;
    /*${ t:borderRadius('0') };*/
    text-shadow: none;
    color: #FFF;
    font-size: 11px;
    font-weight: bold;
    line-height: 15px;
    height: 16px;
    padding: 0 1px;
}

.infobox.infobox-dark > .badge {
    color: #FFF;
    background-color: rgba(255, 255, 255, 0.2) !important;
    border: 1px solid #F1F1F1;
    top: 2px;
    right: 2px;
}

.infobox.infobox-dark > .badge.badge-success > [class*="z-icon-"] {
    color: #C6E9A1;
}

.infobox.infobox-dark > .badge.badge-important > [class*="z-icon-"] {
    color: #ECB792;
}

.infobox.infobox-dark > .badge.badge-warning > [class*="z-icon-"] {
    color: #ECB792;
}

.infobox-small {
    width: 135px;
    height: 52px;
    text-align: left;
    padding-bottom: 5px;
}

.infobox-small > .infobox-icon,
.infobox-small > .infobox-chart,
.infobox-small > .infobox-progress {
    display: inline-block;
    width: 40px;
    height: 42px;
    max-width: 40px;
    line-height: 38px;
    vertical-align: middle;
}

.infobox-small > .infobox-data {
    display: inline-block;
    text-align: left;
    vertical-align: middle;
    max-width: 72px;
    min-width: 0;
}

.infobox-small > .infobox-chart > .sparkline {
    font-size: 14px;
    margin-left: 2px;
}

.percentage {
    font-size: 14px;
    font-weight: bold;
    display: inline-block;
    vertical-align: top;
}

.infobox-small .percentage {
    font-size: 12px;
    font-weight: normal;
    margin-top: 2px;
    margin-left: 2px;
}

.alert.z-window {
    /*${ t:borderRadius('0') };*/
    border-radius: 0px;
}

.alert.z-window .z-caption {
    min-height: 0;
}

.alert.z-window .z-caption-content,
.alert.z-window .z-caption .z-label {
    float: none;
}

.alert.z-window .z-label {
    font-size: 14px;
}

.alert.z-window .z-window-content {
    display: none;
}

.alert.z-window .z-window-icon.z-window-close {
    position: absolute;
    top: 20px;
    right: 15px;
    border: 1px solid #5687a8;
    opacity: 0.2;
    filter: alpha(opacity=20);
    color: #000;
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}

.alert.z-window .z-window-icon.z-window-close:hover,
.alert.z-window .z-window-icon.z-window-close:focus,
.alert.z-window .z-window-icon.z-window-close:active {
    background-color: transparent;
}

.alert.z-window .z-window-icon.z-window-close:hover {
    opacity: 0.5;
    filter: alpha(opacity=50);
}

.alert-success.z-window .z-window-header {
    color: #468847;
    padding: 0;
}

.infobox-container .z-hlayout,
.rtab .z-hlayout {
    white-space: normal;
}

.ie8 .z-panel-icon {
    background-color: #FFF;
}

.z-panel-head {
    /*${ t:borderRadius('0') };*/
    padding: 0;
    border-top-color: #CCC;
    border-left-color: #CCC;
    border-right-color: #CCC;
    border-bottom-color: #DCE8F1;
}

.z-panel-header {
    font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
    padding: 0;
    background: #F7F7F7;
}

.z-panel .z-caption {
    color: #669FC7;
}

.z-panel.transparent .z-panel-head {
    border: 0;
    /*${ t:boxShadow('none') };*/
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}

.z-panel.transparent .z-panel-head .z-panel-header {
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    border-bottom: 1px solid #DCE8F1;
    color: #4383B4;
}

.z-panel.transparent .z-panel-head .z-caption {
    background: transparent;
    color: #669FC7;
}

.z-panel-icon {
    width: 35px;
    height: 37px;
    border: 0;
    /*${ t:borderRadius('0') };*/
    padding: 0 0;
    position: relative;
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    background-color: transparent;
    border-color: #CCC;
}

.z-panel-icon,
.z-panel-icon:hover,
.z-panel-icon:focus,
.z-panel-icon:active {
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    background-color: transparent;
}

.z-panel-icon > i {
    font-size: 14px;
    color: #aaaaaa;
}

.z-panel-icon .z-icon-caret-up:before {
    content: "\f077";
}

.z-panel-icon .z-icon-caret-down:before {
    content: "\f078";
}

.z-panel-icon:before {
    display: inline-block;
    content: "";
    position: absolute;
    top: 3px;
    bottom: 3px;
    left: 0;
    border: 1px solid #D9D9D9;
    border-width: 0 1px 0 0;
}

.traffic .z-panelchildren {
    padding: 9px;
}

.task.z-listbox {
    border-bottom: 1px solid #DDD;
}

.task.z-listbox .z-listitem:hover .z-listitem-checkable {
    border-color: #FF893C;
}

.task.z-listbox .z-listitem-checkbox {
    width: 18px;
    height: 18px;
}

.task.z-listbox .z-listitem {
    border: 1px solid #DDD;
    border-left-width: 3px;
}

.task.z-listbox .z-listitem-selected {
    color: #8090A0;
    background-color: #F4F9FC;
}

.task.z-listbox .z-listitem-selected > .z-listcell,
.task.z-listbox .z-listitem-selected:hover > .z-listcell {
    background-image: none;
}

.task.z-listbox .z-listitem-selected .z-listcell.text .z-listcell-content {
    text-decoration: line-through;
    color: #8090A0;
}

.task.z-listbox .z-listitem .z-listcell + .z-listcell {
    border-left: 0;
}

.task.z-listbox .z-listitem .z-listcell-content {
    padding: 9px;
    height: 45px;
}

.task.z-listbox .z-listitem-icon {
    width: 16px;
    height: 16px;
    line-height: 16px;
    text-align: center;
    padding: 0;
}

.task.z-listbox .z-listitem .z-listitem-icon.z-icon-check:before {
    content: "\f00c";
}

.task.z-listbox .z-listitem,
.task.z-listbox .z-listitem:hover {
    border-left-width: 3px;
}

.task.z-listbox .task-orange {
    border-left-color: #e8b110;
}

.task.z-listbox .task-red {
    border-left-color: #d53f40;
}

.task.z-listbox .task-green {
    border-left-color: #9abc32;
}

.task.z-listbox .task-blue {
    border-left-color: #4f99c6;
}

.task.z-listbox .task-pink {
    border-left-color: #cb6fd7;
}

.task.z-listbox .task-grey {
    border-left-color: #a0a0a0;
}

.task.z-listbox .task-default {
    border-left-color: #abbac3;
}

.conversation.z-panel .z-caption {
    padding-left: 12px;
}

.conversation.z-panel .z-caption-content {
    font-size: 17px;
    line-height: 36px;
}

.conversation.z-panel .body > .text {
    padding-left: 0px;
    padding-bottom: 0px;
}

.conversation.z-panel .body > .text:after {
    display: none;
}

.conversation.z-panel .action {
    border-top: 1px solid #E5E5E5;
    background-color: #F5F5F5;
    padding: 10px 12px 12px 12px;
}

.conversation.z-panel .action .z-textbox {
    height: 34px;
    font-family: 'Open Sans';
    /*${ t:borderRadius('0') };*/
    color: #858585;
    border: 1px solid #D5D5D5;
    padding: 5px 4px;
    line-height: 1.2;
    font-size: 14px;
    -webkit-box-shadow: none;
    box-shadow: none;
    -webkit-transition-duration: 0.1s;
    transition-duration: 0.1s;
}

.conversation.z-panel .action .z-textbox:hover {
    border-color: #B5B5B5;
}

.conversation.z-panel .action .z-textbox:focus {
    -webkit-box-shadow: none;
    box-shadow: none;
    color: #696969;
    border-color: #F59942;
    outline: none;
}

.conversation.z-panel .action .z-button {
    height: 34px;
}

.body {
    position: relative;
    width: auto;
    margin-right: 12px;
}

.body > .name {
    display: block;
}

.body > .name:hover {
    text-decoration: underline;
}

.body > .text {
    display: block;
    position: relative;
    margin-top: 2px;
    padding-bottom: 19px;
    padding-left: 7px;
    font-size: 12px;
}

.body > .text > [class*="z-icon-quote-"]:first-child {
    color: #DCE3ED;
    margin-right: 4px;
}

.body > .text:after {
    display: block;
    content: '';
    height: 1px;
    font-size: 0px;
    overflow: hidden;
    position: absolute;
    left: 16px;
    right: -12px;
    margin-top: 9px;
    border-top: 1px solid #e4ecf3;
}

.body > .tools {
    right: 9px;
    bottom: 10px;
}

.time {
    display: block;
    font-size: 11px;
    font-weight: bold;
    position: absolute;
    right: 9px;
    top: 0;
    cursor: default;
}

.time [class*="z-icon-"] {
    font-size: 14px;
    color: #666666;
    margin-right: 4px;
}

.comments.z-grid .user > .z-image {
    border-color: #5293C4;
}

.comments.z-grid .z-row:hover .tools {
    display: inline-block;
}

.comments.z-grid .z-row-content {
    padding: 0;
    min-height: 66px;
    position: relative;
}

.dialog.z-grid {
    border-spacing: 12px;
}

.dialog.z-grid:before {
    display: block;
    width: 3px;
    height: 100%;
    position: absolute;
    content: "";
    left: 30px;
    max-width: 3px;
    background-color: #E1E6ED;
    border: 1px solid #D7DBDD;
    border-width: 1px 1px;
}

.dialog.z-grid .z-grid-body > table {
    border-collapse: separate;
    border-spacing: 3px 12px;
}

.dialog.z-grid .z-row-inner {
    vertical-align: top;
    overflow: visible;
}

.dialog.z-grid .z-row-content {
    padding: 0;
    overflow: visible;
}

.dialog.z-grid .z-grid-body {
    padding: 9px 9px 0 9px;
}

.dialog.z-grid .z-row {
    min-height: 66px;
    position: relative;
}

.dialog.z-grid .z-row:hover .tools {
    display: inline-block;
}

.dialog.z-grid .body {
    position: relative;
    width: auto;
    padding: 5px 8px 8px;
    border: 1px solid #DDE4ED;
    border-left-width: 2px;
    margin-right: 1px;
}

.dialog.z-grid .body > .name {
    display: block;
}

.dialog.z-grid .body > .name:hover {
    text-decoration: underline;
}

.dialog.z-grid .body .tools {
    bottom: 4px;
    /*${ t:borderRadius('36px') };*/
    margin: 1px 0px;
}

.dialog.z-grid .body:before {
    content: "";
    display: block;
    width: 8px;
    height: 8px;
    position: absolute;
    left: -7px;
    top: 11px;
    border: 2px solid #DDE4ED;
    border-width: 2px 0 0 2px;
    background-color: #FFF;
    -webkit-box-sizing: content-box;
    -moz-box-sizing: content-box;
    box-sizing: content-box;
    -webkit-transform: rotate(-45deg);
    -ms-transform: rotate(-45deg);
    transform: rotate(-45deg);
}

.tools {
    position: absolute;
    right: 5px;
    display: none;
    bottom: 4px;
}

.tools .btn {
    /*${ t:borderRadius('36px') };*/
    margin: 1px 0;
}

.action a {
    margin: 0 3px;
    display: inline-block;
    opacity: 0.85;
    filter: alpha(opacity=85);
    -webkit-transition: all 0.1s;
    transition: all 0.1s;
}

.action a:hover {
    text-decoration: none;
    opacity: 1;
    filter: alpha(opacity=100);
}

.member.z-div {
    width: 175px;
    height: 64px;
    padding: 2px;
    margin: 3px 0px;
    border-bottom: 1px solid #E8E8E8;
}

.member.z-div > .user {
    position: absolute;
    left: 0;
}

.member.z-div > .user > img {
    border-color: #DCE3ED;
}

.member.z-div > .body {
    margin-left: 50px;
}

.member.z-div > .body > .time {
    position: static;
}

.member.z-div > .body > .name {
    line-height: 18px;
    height: 18px;
    margin-bottom: 0;
}

.member.z-div > .body > .name > a {
    display: inline-block;
    max-width: 100px;
    max-height: 18px;
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
}

.member.z-div > .body .relin {
    display: inline-block;
    position: relative;
}

body {
    padding: 0;
    background-color: rgb(78, 116, 149);
    min-height: 100%;
    font-family: 'Open Sans';
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #fff;
    line-height: 1.5;
}

.navbar {
    margin: 0;
    padding: 0 10px;
    border: 0;
    min-height: 45px;
    position: relative;
    background: rgb(78, 116, 149);
    /*${ t:borderRadius('0') };*/
}

.navbar-brand {
    color: #ffffff;
    font-size: 20px;
    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
    padding-top: 0px;
    padding-bottom: 0px;
    letter-spacing: 1px;
}

.navbar-brand:hover,
.navbar-brand:focus {
    color: #ffffff;
}

.z-east-splitter,
.z-west-splitter,
.z-north-splitter,
.z-south-splitter {
    width: 8px;
    height: 8px;
    background: rgb(249, 249, 249);
    /*background: rgb(227,227,227);*/

    position: absolute;
    overflow: hidden;
    cursor: e-resize;
}

.z-north-noborder, .z-south-noborder, .z-west-noborder, .z-center-noborder, .z-east-noborder {
    border: 0
}

.z-north-header, .z-south-header, .z-west-header, .z-center-header, .z-east-header {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 12px;
    font-weight: bold;
    font-style: normal;
    color: #2c3e50;
    height: 38px;
    border-bottom: 1px solid rgb(227, 227, 227);
    padding: 5px 4px 3px;
    line-height: 35px;
    background: rgb(227, 227, 227);

    overflow: hidden;
    cursor: default;
    white-space: nowrap
}

.c-center, .c-north, .c-south, .c-east {
    border: none;
    padding: 5px 8px 5px 8px;
}

.c-north {
    padding: 10px 8px 0px 8px;
}

.c-center {
    padding: 5px 8px 0px 8px;
}

.c-south {
    padding: 2px 8px 2px 8px;
}

.c-east {
    padding: 2px 8px 2px 8px;
}

/* busy等待窗口 设置 */
.z-loading-indicator {
    padding: 6px;
}

.z-loading-indicator, .z-apply-loading-indicator {
    color: #333;
    border: 1px solid #ccc;
    background: #fff;
    white-space: pre-wrap;
}

.z-apply-loading-icon, .z-loading-icon {
    display: inline-block;
    vertical-align: top;
}

.z-apply-loading-icon, .z-loading-icon, .z-renderdefer {
    width: 18px;
    height: 18px;
    margin-right: 5px;
    background: transparent no-repeat center;
    background-image: url(/poms/resource/images/busy_20x20.gif);
}

input[type="radio"], input[type="checkbox"] {
    margin: 4px 0 0;
    margin-top: 1px;
    line-height: normal;
    vertical-align: baseline;
}
