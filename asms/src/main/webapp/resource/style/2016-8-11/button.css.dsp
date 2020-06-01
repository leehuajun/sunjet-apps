.c-button {
    color: #ffffff;
    font-weight: 700;
    width: 80px;
    height: 25px
}

.inner-button {
    color: #ffffff;
    font-weight: 700;
    width: 20px;
    height: 18px;
    /*padding-left: 1px;*/
    padding: 2px 4px !important;
    font-size: 14px !important;;
}

.z-button {
    position: relative;
    padding: 3px 12px;
    border-radius: 0px;
    /*${ t:borderRadius('0px') };*/
    text-shadow: 0 0 0 rgba(0, 0, 0, 0.25);
    min-height: 0;
    /*color: #FFF !important;*/
    /*${ t:applyCSS3('transition', 'all ease 0.15s 0') };*/
}

.z-button:active {
    top: 1px;
    left: 1px;
}

.z-button,
.z-button:hover,
.z-button:focus,
.z-button:active {
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}

.z-button > [class*="z-icon-"] {
    display: inline;
    margin-right: 4px;
}

.z-button > .icon-only[class*="z-icon-"] {
    margin: 0;
    vertical-align: middle;
    text-align: center;
    padding: 0;
}

.z-combobox-button, .z-bandbox-button, .z-datebox-button, .z-timebox-button, .z-spinner-button, .z-doublespinner-button {
    border-radius: 0px;
    color: #6fb3e0;
    background-color: #FFFFFF !important;
}

.btn {
    color: #FFFFFF !important;
}

.btn-sm {
    border-width: 4px;
    font-size: 12px;
    padding: 4px 9px;
    line-height: 1.39;
}

.btn-xs {
    border-width: 3px;
}

.btn-minier {
    padding: 0 4px;
    line-height: 18px;
    border-width: 2px;
    font-size: 12px;
}

.btn-primary,
.btn-primary:focus {
    background-color: rgb(78, 116, 149) !important;
    border-color: rgb(78, 116, 149);
}

.btn-primary:hover,
.btn-primary.open {
    background-color: #1b6aaa !important;
    border-color: #428bca;
}

.btn-primary.no-border:hover {
    border-color: #1b6aaa;
}

.btn-primary.no-hover:hover {
    background-color: #428bca !important;
}

.btn-info,
.btn-info:focus {
    background-color: #6fb3e0 !important;
    border-color: #6fb3e0;
}

.btn-info:hover,
.btn-info.open {
    background-color: #4f99c6 !important;
    border-color: #6fb3e0;
}

.btn-info.no-border:hover {
    border-color: #4f99c6;
}

.btn-info.no-hover:hover {
    background-color: #6fb3e0 !important;
}

.btn-success,
.btn-success:focus {
    background-color: #87b87f !important;
    border-color: #87b87f;
}

.btn-success:hover,
.btn-success.open {
    background-color: #629b58 !important;
    border-color: #87b87f;
}

.btn-success.no-border:hover {
    border-color: #629b58;
}

.btn-success.no-hover:hover {
    background-color: #87b87f !important;
}

.btn-warning,
.btn-warning:focus {
    background-color: #ffb752 !important;
    border-color: #ffb752;
}

.btn-warning:hover,
.btn-warning.open {
    background-color: #e59729 !important;
    border-color: #ffb752;
}

.btn-warning.no-border:hover {
    border-color: #e59729;
}

.btn-warning.no-hover:hover {
    background-color: #ffb752 !important;
}

.btn-danger,
.btn-danger:focus {
    background-color: #d15b47 !important;
    border-color: #d15b47;
}

.btn-danger:hover,
.btn-danger.open {
    background-color: #b74635 !important;
    border-color: #d15b47;
}

.btn-danger.no-border:hover {
    border-color: #b74635;
}

.btn-danger.no-hover:hover {
    background-color: #d15b47 !important;
}

.btn-inverse,
.btn-inverse:focus {
    background-color: #555555 !important;
    border-color: #555555;
}

.btn-inverse:hover,
.btn-inverse.open {
    background-color: #303030 !important;
    border-color: #555555;
}

.btn-inverse.no-border:hover {
    border-color: #303030;
}

.btn-inverse.no-hover:hover {
    background-color: #555555 !important;
}

.btn-pink,
.btn-pink:focus {
    background-color: #d6487e !important;
    border-color: #d6487e;
}

.btn-pink:hover,
.btn-pink.open {
    background-color: #b73766 !important;
    border-color: #d6487e;
}

.btn-pink.no-border:hover {
    border-color: #b73766;
}

.btn-pink.no-hover:hover {
    background-color: #d6487e !important;
}

.btn-yellow {
    color: #996633 !important;
    text-shadow: 0 -1px 0 rgba(255, 255, 255, 0.4) !important;
}

.btn-yellow,
.btn-yellow:focus {
    background-color: #fee188 !important;
    border-color: #fee188;
}

.btn-yellow:hover,
.btn-yellow.open {
    background-color: #f7d05b !important;
    border-color: #fee188;
}

.btn-yellow.no-border:hover {
    border-color: #f7d05b;
}

.btn-yellow.no-hover:hover {
    background-color: #fee188 !important;
}

.btn-sm > [class*="z-icon-"] {
    margin-right: 3px;
}

.btn-sm > [class*="z-icon-"].icon-on-right {
    margin-right: 0;
    margin-left: 3px;
}

.btn-xs > [class*="z-icon-"],
.btn-minier > [class*="z-icon-"] {
    margin-right: 2px;
}

.z-button.btn-no-border {
    border-width: 0 !important;
}