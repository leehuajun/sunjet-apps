.ctoolbars {
    position: relative;
    border: 0px solid rgb(227, 227, 227);
    background-color: rgb(255, 255, 255);
    min-height: 28px;
    line-height: 28px;
    padding: 2px 0px 2px 0px;
    margin-right: 0px;
}

.ctoolbar {
    background-color: transparent;
    display: inline-block;
    line-height: 24px;
    margin: 0px 0px 0 0px;
    padding: 0;
    color: #333;
}

.breadcrumbs {
    position: relative;
    border-bottom: 1px solid rgb(227, 227, 227);
    background-color: rgb(255, 255, 255);
    min-height: 35px;
    line-height: 32px;
    padding: 10px 5px 5px 0;
}

.breadcrumb {
    background-color: transparent;
    display: inline-block;
    line-height: 24px;
    margin: 0 22px 0 12px;
    padding: 0;
    color: #333;
}

.breadcrumb > .z-a {
    color: #4C8FBD;
}

.breadcrumb > .z-a:hover,
.breadcrumb > .z-a:focus {
    text-decoration: none;
}

.breadcrumb > .z-a + .z-label {
    padding-right: 4px;
}

.breadcrumb > .z-a + .z-label:before {
    font-family: FontAwesome;
    font-weight: normal;
    font-style: normal;
    text-decoration: inherit;
    -webkit-font-smoothing: antialiased;
    *margin-right: .3em;
    font-size: 14px;
    color: #b2b6bf;
    content: "\f105";
    margin-right: 2px;
    padding: 0 5px 0 2px;
    position: relative;
    top: 1px;
}

.breadcrumb .z-label,
.breadcrumb .home-icon {
    color: #555;
}

.breadcrumb .home-icon {
    margin-left: 4px;
    margin-right: 2px;
    font-size: 20px;
    position: relative;
    top: 2px;
    cursor: default;
}

.nav-search {
    position: absolute;
    top: 0px;
    right: 0px;
    line-height: 24px;
}

.nav-search .z-bandbox-input {
    font-family: 'Open Sans';
    font-size: 12px;
    font-weight: normal;
    font-style: normal;
    color: #666666;
    width: 160px;
    height: 28px;
    border: 1px solid rgb(135, 184, 127);
    /*${ t:borderRadius('0px') };*/
    padding-left: 24px;
    padding-right: 6px;
    border-radius: 0 0 0 0;
}

.nav-search .z-bandbox-input:focus {
    border: 2px solid rgb(135, 184, 127);
    /*${ t:boxShadow('none') };*/
}

.nav-search .z-bandbox-button {
    /*${ t:borderRadius('0px') };*/
    position: absolute;
    top: 2px;
    bottom: 1px;
    left: 3px;
    min-width: 19px;
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    /*line-height: auto;*/
    padding: 0;
}

.nav-search .z-bandbox-button,
.nav-search .z-bandbox-button:hover,
.nav-search .z-bandbox-button:focus,
.nav-search .z-bandbox-button:active {
    background-color: transparent;
    border: 0;
    /*${ t:boxShadow('none') };*/
}

.nav-search .z-bandbox-button [class*="z-icon-"] {
    font-size: 14px;
    color: #6fb3e0;
    line-height: 24px;
}