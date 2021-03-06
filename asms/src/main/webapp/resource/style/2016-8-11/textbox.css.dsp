/*<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>*/
/*<%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %>*/
/*<%@ taglib uri="http://www.zkoss.org/dsp/web/theme" prefix="t" %>*/

a:hover, a:visited, a:active, a:focus {
    text-decoration: none;
}

/*a:link {*/
/*text-decoration: none;*/
/*}*/
/*a:visited {*/
/*text-decoration: none;*/
/*}*/
/*a:hover {*/
/*text-decoration: none;*/
/*}*/
/*a:active {*/
/*text-decoration: none;*/
/*}*/

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

.z-textbox {
    border-radius: 0px;
}

.z-textbox, .z-decimalbox, .z-intbox, .z-longbox, .z-doublebox {
    -moz-box-shadow: none;
    box-shadow: none;
}

.z-textbox:focus, .z-decimalbox:focus, .z-intbox:focus, .z-longbox:focus, .z-doublebox:focus {
    -moz-box-shadow: none;
    box-shadow: none;
    border: 1px solid #6fb3e0;
}

.z-textbox[readonly]:focus, .z-decimalbox[readonly]:focus, .z-intbox[readonly]:focus, .z-longbox[readonly]:focus, .z-doublebox[readonly]:focus {
    -moz-box-shadow: none;
    box-shadow: none;
    border: 1px solid #8d989a;
}

.z-combobox-input, .z-bandbox-input, .z-datebox-input, .z-timebox-input, .z-spinner-input, .z-doublespinner-input {
    -moz-box-shadow: none;
    box-shadow: none;
    border-radius: 0px;
}

.z-combobox-input:focus, .z-bandbox-input:focus, .z-datebox-input:focus, .z-timebox-input:focus, .z-spinner-input:focus, .z-doublespinner-input:focus {
    -moz-box-shadow: none;
    box-shadow: none;
    border: 1px solid #6fb3e0;
    border-radius: 0px;
}
