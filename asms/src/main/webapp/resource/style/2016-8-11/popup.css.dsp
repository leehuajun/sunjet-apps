.z-popup,
.z-menupopup {
    /*${ t:boxShadow('0 2px 4px rgba(0, 0, 0, 0.2)') };*/
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    background-color: #FFF;
}

.z-popup-content,
.z-menupopup-content {
    padding: 5px 0;
}

.z-popup:before,
.z-menupopup:before {
    border-bottom: 7px solid rgba(0, 0, 0, 0.2);
    border-left: 7px solid transparent;
    border-right: 7px solid transparent;
    content: "";
    display: inline-block;
    right: 9px;
    position: absolute;
    top: -7px;
}

.z-popup:after,
.z-menupopup:after {
    border-bottom: 6px solid #FFFFFF;
    border-left: 6px solid transparent;
    border-right: 6px solid transparent;
    content: "";
    display: inline-block;
    right: 10px;
    position: absolute;
    top: -6px;
}

.z-popup {
    overflow: visible;
}

.menu.z-popup .z-popup-content {
    padding: 0;
}

.menu.z-popup .z-popup-content .header {
    padding: 0 8px;
    display: block;
    background-color: #ECF2F7;
    border-bottom: 1px solid #BCD4E5;
    color: #8090A0;
    font-weight: bold;
    line-height: 34px;
    cursor: default;
}

.menu.z-popup .z-popup-content .z-vlayout-inner {
    padding: 0 8px;
}

.menu.z-popup .z-popup-content .z-vlayout-inner:hover {
    background-color: #F4F9FC;
    color: #555;
}

.menu.z-popup .z-popup-content .z-vlayout-inner .z-a {
    display: block;
    padding: 10px 2px;
    color: #555;
    border-bottom: 1px solid #E4E6E9;
}

.menu.z-popup .z-popup-content .z-vlayout-inner .z-a .z-label {
    font-size: 12px;
    line-height: 16px;
    height: 16px;
}

.menu.z-popup .z-popup-content .z-vlayout-inner .z-a .z-progressmeter {
    margin-top: 4px;
}

.menu.z-popup .z-popup-content .z-vlayout-inner:last-child > .z-a {
    border-bottom-width: 0;
    border-top: 1px dotted transparent;
    color: #4F99C6;
    text-align: center;
}

.menu.z-popup .z-popup-content .z-vlayout-inner:last-child > .z-a:hover {
    text-decoration: none;
}

.menu.z-popup .z-popup-content .z-vlayout-inner:last-child > .z-a:hover [class*="z-icon-"] {
    text-decoration: none;
}

.menu.z-popup .z-popup-content .z-vlayout-inner:last-child > .z-a [class*="z-icon-"] {
    margin-left: 6px;
    color: #555;
}

.menu.z-popup .z-popup-content .msg-photo {
    margin-right: 6px;
    max-width: 42px;
}

.menu.z-popup .z-popup-content .msg-body {
    display: inline-block;
    line-height: 20px;
    white-space: normal;
    vertical-align: middle;
    max-width: 170px;
    font-size: 12px;
}

.menu.z-popup .z-popup-content .msg-title {
    display: inline-block;
    line-height: 14px;
}

.menu.z-popup .z-popup-content .msg-time {
    display: block;
    font-size: 11px;
    color: #777;
}

.menu.z-popup .z-popup-content .msg-time > [class*="z-icon-"] {
    font-size: 14px;
    color: #555555;
    margin-right: 2px;
}

.menu-pink.z-popup .z-popup-content .header {
    background-color: #F7ECF2;
    border-bottom: 1px solid #E5BCD4;
    color: #B471A0;
}

.menu-pink.z-popup .z-popup-content .header > [class*="z-icon-"] {
    color: #C06090;
}

.menu-pink.z-popup .z-popup-content .z-vlayout-inner:hover {
    background-color: #FCF4F9;
}

.menu-pink.z-popup .z-popup-content .z-vlayout-inner .z-a {
    border-bottom: 1px solid #F3E4EC;
}

.menu-pink.z-popup .z-popup-content .z-vlayout-inner:last-child > .z-a {
    color: #4F99C6;
}

.z-menupopup {
    padding: 0;
    width: 160px;
}

.z-menupopup * {
    text-shadow: none;
    background-image: none;
    border: 0;
}

.z-menupopup .z-menuitem:hover,
.z-menupopup .z-menuitem-content:hover,
.z-menupopup .z-menuitem:active,
.z-menupopup .z-menuitem-content:active {
    background-image: none;
    /*${ t:boxShadow('none') };*/
}

.z-menupopup .z-menupopup-separator {
    display: none;
}

.z-menupopup .z-menuitem-content {
    padding: 4px 11px;
    margin: 1px 4px;
    line-height: 14px;
    color: #333;
}

.z-menupopup .z-menuitem-content:hover,
.z-menupopup .z-menuitem-content:focus {
    background-color: #FEE188;
    color: #444;
    /*${ t:borderRadius('0') };*/
}

.z-menupopup .z-menuitem-content [class*="z-icon-"] {
    margin-right: 6px;
    font-size: 16px;
    display: inline-block;
    text-align: center;
}

.z-menupopup .z-menuseparator {
    margin: 9px;
}

.z-menupopup .z-menupopup-content {
    padding: 5px 0;
    margin: 2px 0 0;
}
