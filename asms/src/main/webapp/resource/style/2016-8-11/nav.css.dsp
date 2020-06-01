.navbar {
    margin-bottom: 0;
    padding-left: 0;
    padding-right: 0;
    margin: 0;
    border: none;
    /*${ t:boxShadow('none') };*/
    border-radius: 0;
    min-height: 45px;
    position: relative;
    background: rgb(78, 116, 149);
}

.navbar .navbar-brand {
    color: #ffffff;
    font-size: 20px;
    text-shadow: none;
    padding-top: 10px;
    padding-bottom: 10px;
}

.navbar .navbar-brand:hover,
.navbar .navbar-brand:focus {
    color: #ffffff;
}

.user {
    display: inline-block;
    width: 42px;
}

.user > .z-image {
    /*${ t:borderRadius('100%') };*/
    border: 2px solid #C9D6E5;
    max-width: 40px;
}

.nav-user > div > a,
.nav-user > div .user-menu {
    background-color: rgb(78, 116, 149);
    color: #FFF;
    display: block;
    line-height: 45px;
    height: 45px;
    border-left: 1px solid rgb(44, 64, 82);
    text-align: center;
    height: 100%;
    width: auto;
    min-width: 50px;
    padding: 0 8px;
    position: relative;
    vertical-align: middle;
}

.nav-user > div > a .z-menu-selected,
.nav-user > div .user-menu .z-menu-selected {
    color: #FFF !important;
}

.nav-user > div > a.grey,
.nav-user > div .user-menu.grey {
    background-color: #555555 ! important;
}

.nav-user > div > a.grey.open,
.nav-user > div .user-menu.grey.open,
.nav-user > div > a.grey:hover,
.nav-user > div .user-menu.grey:hover,
.nav-user > div > a.grey:focus,
.nav-user > div .user-menu.grey:focus,
.nav-user > div > a.grey .z-menu-selected,
.nav-user > div .user-menu.grey .z-menu-selected {
    background-color: #4b4b4b ! important;
}

.nav-user > div > a.purple,
.nav-user > div .user-menu.purple {
    background-color: #892e65 ! important;
}

.nav-user > div > a.purple.open,
.nav-user > div .user-menu.purple.open,
.nav-user > div > a.purple:hover,
.nav-user > div .user-menu.purple:hover,
.nav-user > div > a.purple:focus,
.nav-user > div .user-menu.purple:focus,
.nav-user > div > a.purple .z-menu-selected,
.nav-user > div .user-menu.purple .z-menu-selected {
    background-color: #762c59 ! important;
}

.nav-user > div > a.green,
.nav-user > div .user-menu.green {
    background-color: #2e8965 ! important;
}

.nav-user > div > a.green.open,
.nav-user > div .user-menu.green.open,
.nav-user > div > a.green:hover,
.nav-user > div .user-menu.green:hover,
.nav-user > div > a.green:focus,
.nav-user > div .user-menu.green:focus,
.nav-user > div > a.green .z-menu-selected,
.nav-user > div .user-menu.green .z-menu-selected {
    background-color: #2c7659 ! important;
}

.nav-user > div > a.light-blue,
.nav-user > div .user-menu.light-blue {
    background-color: #62a8d1 ! important;
}

.nav-user > div > a.light-blue.open,
.nav-user > div .user-menu.light-blue.open,
.nav-user > div > a.light-blue:hover,
.nav-user > div .user-menu.light-blue:hover,
.nav-user > div > a.light-blue:focus,
.nav-user > div .user-menu.light-blue:focus,
.nav-user > div > a.light-blue .z-menu-selected,
.nav-user > div .user-menu.light-blue .z-menu-selected {
    background-color: #579ec8 ! important;
}

.nav-user > div > a.margin-4,
.nav-user > div .user-menu.margin-4 {
    margin-left: 4px;
}

.nav-user > div > a.margin-3,
.nav-user > div .user-menu.margin-3 {
    margin-left: 3px;
}

.nav-user > div > a.margin-2,
.nav-user > div .user-menu.margin-2 {
    margin-left: 2px;
}

.nav-user > div > a.margin-1,
.nav-user > div .user-menu.margin-1 {
    margin-left: 1px;
}

.nav-user > div > a.no-border,
.nav-user > div .user-menu.no-border {
    border: none !important;
}

.nav-user > div .badge {
    position: relative;
    top: -4px;
    left: 2px;
    padding-right: 5px;
    padding-left: 5px;
    color: whitesmoke;
}

.nav-user > div > a:hover,
.nav-user > div > a:focus {
    background-color: #2c5976;
}

.nav-user > div .user-menu.z-menubar {
    padding: 0;
    border: 0;
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
    border-left: 1px solid rgb(44, 64, 82);
}

.nav-user > div .user-menu.z-menubar img {
    margin: -4px 8px 0 0;
    /*${ t:borderRadius('100%') };*/
    #border: 2px solid #FFF;
    max-width: 40px;
}

.nav-user > div .user-menu.z-menubar .z-menu-content {
    line-height: 45px;
    height: 45px;
    border: 0;
    padding: 0 8px;
}

.nav-user > div .user-menu.z-menubar .z-menu-content [class*="z-icon-"] {
    display: inline-block;
    width: 1.25em;
    text-align: center;
}

.nav-user > div .user-menu.z-menubar .z-menu:hover,
.nav-user > div .user-menu.z-menubar .z-menu-content:hover,
.nav-user > div .user-menu.z-menubar .z-menu:focus,
.nav-user > div .user-menu.z-menubar .z-menu-content:focus {
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}

.nav-user > div .user-menu.z-menubar .z-menu {
    margin: 0;
}

.nav-user > div .user-menu.z-menubar .z-menu-selected > .z-menu-content,
.nav-user > div .user-menu.z-menubar .z-menu-content:active {
    border: 0;
    /*${ t:boxShadow('none') };*/
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}

.nav-user > div .user-menu.z-menubar .z-menu-text {
    max-width: 80px;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
    text-align: left;
    vertical-align: top;
    line-height: 15px;
    position: relative;
    top: 6px;
    color: #FFF;
    font-size: 12px;
}

.nav-user > div .user-menu.z-menubar .z-menu-icon {
    top: 0;
    right: 8px;
}

.nav-user > div .user-menu.z-menubar .z-menu-text {
    text-shadow: none;
}

.nav-user > div [class*="z-icon-"] {
    font-size: 16px;
    color: #ffffff;
    display: inline-block;
    width: 20px;
    text-align: center;
}

.nav-user > div:first-child > a {
    #border-left: none;
}

.nav-user > div:last-child > a {
    border-right: 1px solid rgb(44, 64, 82);
}

.badge {
    text-shadow: none;
    font-size: 12px;
    padding-top: 1px;
    padding-bottom: 3px;
    font-weight: normal;
    line-height: 15px;
    background-color: rgb(44, 64, 82) !important;
}

.label-grey,
.badge-grey {
    background-color: #a0a0a0 !important;
}

.label-info,
.badge-info {
    background-color: #3a87ad !important;
}

.label-primary,
.badge-primary {
    background-color: #428bca !important;
}

.label-success,
.badge-success {
    background-color: #82af6f !important;
}

.label-danger,
.badge-danger {
    background-color: #d15b47 !important;
}

.label-important,
.badge-important {
    background-color: #d15b47 !important;
}

.label-inverse,
.badge-inverse {
    background-color: #333333 !important;
}

.label-warning,
.badge-warning {
    background-color: #f89406 !important;
}

.label-pink,
.badge-pink {
    background-color: #d6487e !important;
}

.label-purple,
.badge-purple {
    background-color: #9585bf !important;
}

.label-yellow,
.badge-yellow {
    background-color: #fee188 !important;
}

.label-light,
.badge-light {
    background-color: #e7e7e7 !important;
}

.badge-yellow,
.label-yellow {
    color: #996633 !important;
    border-color: #fee188;
}

.badge-default,
.label-default {
    color: #996633 !important;
    border-color: #fee188;
}