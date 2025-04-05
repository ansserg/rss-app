//------------------------ константы -------------------------
const server_name = "http://msk-rss-appa0.megafon.ru";
const server_port = "8400";
const url_login = "rssvw/login";
const url_logout = "rssvw/logout";
const url_change_password="rssvw/passwd";
const url_trf_price = "rssvw/trafficprice";
const url_rtpl_name = "rssvw/rpname";
const alertPlaceholder = document.getElementById('alertPlaceholder');
const navbarUser = document.querySelector("#navbar-user");

linkLogIn = document.querySelector("#link-logIn");
linkLogOut = document.querySelector("#link-logOut");
linkTariffy = document.querySelector("#link-tariffy");
logInModal = document.querySelector("#logInModal");
tariffDataModal = document.querySelector("#tariffDataModal");
btnSendTariffParam = document.querySelector("#btn-sendTariffParam");
btnlogIn = document.querySelector("#btn-logIn");
btnLogInCancel = document.querySelector("#btn-logIn-cancel");
WinLogInModal = new bootstrap.Modal(logInModal);
WintariffDataModal = new bootstrap.Modal(tariffDataModal);
flagNewPassword = document.querySelector("#flagNewPassword");
divNewPassword = document.querySelector("#divNewPassword");
navbarContent=document.querySelector("#navbarListContent");


window.addEventListener("beforeunload", (e) => {
    e.preventDefault();
    logOut();
    e.returnValue = true;
});

logInModal.addEventListener("hide.bs.modal", function (e) {
    console.log("eventListener:hide.bs.modal");
});

logInModal.addEventListener("show.bs.modal", function (e) {
    document.querySelector("#inputLogIn").value = "";
    document.querySelector("#inputPassword").value = "";
    document.querySelector("#inputNewPassword").value = "";
    document.querySelector("#statusBar").innerHTML = "";
    flagNewPassword.checked=false;
    divNewPasswordDisplay();
    console.log("show modal logIn");
});

tariffParamModal.addEventListener("show.bs.modal", function (e) {
    console.log("eventListener:showTariffModalParam");
    document.querySelector("#alertPlaceholder").innerHTML = "";
    document.querySelector("#tariffParamValue").value = "";
    document.querySelector('#tariffStartDate').value = cur_date();
});

tariffParamModal.addEventListener("hidden.bs.modal", function (e) {
    // const elementModal=document.querySelector("#tariffDataModal");
    // const dataModal= new bootstrap.Modal(elementModal,{

    // });
    console.log("eventListener:hiddenTariffParamModal");
    // getRatePlanData(e);
    // dataModal.show();
});

tariffDataModal.addEventListener("hidden.bs.modal", function (e) {
    const t_body = document.querySelector("#tbodyTariffData");
    t_body.innerHTML = "";
    console.log("eventListener:hiddenTariffDataModal");
});

flagNewPassword.onclick = (event) => {
    divNewPasswordDisplay();
}

btnlogIn.onclick = (event) => {
    logIn(event);
};

btnSendTariffParam.onclick = (event) => {
    event.preventDefault();
    getRatePlanData();
}

linkLogIn.onclick = (event) => {
    event.preventDefault();
    WinLogInModal.show();
}

linkLogOut.onclick = (event) => {
    event.preventDefault();
    logOut();
}

function divNewPasswordDisplay() {
    if (flagNewPassword.checked) {
        console.log("flagNewPassword checked");
        divNewPassword.classList.remove("d-none")
    } else {
        console.log("flagNewPassword not checked")
        divNewPassword.classList.add("d-none");
    }
}

function init() {
    console.log("init");
    alertPlaceholder.innerHTML = "";
    logOut();
    WinLogInModal.show();
    linkLogOut.classList.add("d-none");
    navbarContent.classList.add("d-none");
}

const alert_output = (message, type) => {
    const wrapper = document.createElement('div')
    wrapper.innerHTML = [
        `<div class="alert alert-${type} alert-dismissible" role="alert">`,
        `   <div>${message}</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('');
    alertPlaceholder.append(wrapper);
}

function xhr_req(p_method, p_url, p_exec, p_data) {
    let xhr = new XMLHttpRequest();
    xhr.open(p_method, p_url, true);
    // xhr.withCredentials = true;
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    if (p_method.toUpperCase() == 'POST') {
        xhr.send(p_data);
        console.log("xhr_req:POST:" + p_data);
    } else {
        xhr.send();
        console.log("xhr_req:GET");
    }
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                p_exec(null, this.response);
            } else {
                p_exec('err', this.response);
            }
        }
    }
}

function getRatePlanData() {
    // e.preventDefault();
    let paramValue = document.querySelector('#tariffParamValue').value;
    let startDate = document.querySelector('#tariffStartDate').value;
    // let tp = document.querySelectorAll('input[name="inp_type"]');
    val_type = "name";
    // console.log(tp);
    // for (let i = 0; i < tp.length; i++) {
    //     if (tp[i].checked) {
    //         val_type = tp[i].value;
    //     }
    // }
    console.log(`paramValue=${paramValue}`);
    console.log(`startDate=${startDate}`);
    xhr_req('POST', `${server_name}:${server_port}/${url_trf_price}`, priceListData, `val_tp=${val_type}&value=${paramValue}&date=${startDate}`);
}

function priceListData(err, data) {
    data = JSON.parse(data);
    if (!err) {
        const startDate = document.querySelector('#tariffStartDate').value;
        const t_body = document.querySelector("#tbodyTariffData");
        console.log(data);
        data.forEach(el => {
            const t_row = document.createElement("tr");
            add_tbl_price_ceil(t_row, document.createTextNode(el.tarifName));
            add_tbl_price_ceil(t_row, document.createTextNode(el.price));
            add_tbl_price_ceil(t_row, document.createTextNode(el.rndtName));
            add_tbl_price_ceil(t_row, document.createTextNode(el.startDate));
            add_tbl_price_ceil(t_row, document.createTextNode(el.endDate));
            t_body.appendChild(t_row);
        });
        xhr_req('GET', `${server_name}:${server_port}/${url_rtpl_name}/${data[0].rtplId}?&date=${startDate}`, getRPName);
        WintariffDataModal.show();
    } else {
        console.log(data.message);
        if (data.status == 401) {
            WinLogInModal.show();
        } else if (data.status == 404) {
            alert_output(data.message, "warning");
        }
        else {
            console.log(data.error + " " + data.message);
            alert_output(data.error + ": " + data.message, "warning");
        }
    }
}

function add_tbl_price_ceil(row, val) {
    const ceil = document.createElement("td");
    ceil.appendChild(val);
    row.appendChild(ceil);
}

function getRPName(err, data) {
    console.log(data);
    document.querySelector('#tariffDataModalLabel').innerHTML = `Тарифный план:${data}`;
}

function cur_date() {
    let cur_date = new Date();
    let ret_val = cur_date.getFullYear() + '-' + zero_first_format(cur_date.getMonth() + 1) + '-' + zero_first_format(cur_date.getDate());
    console.log(`cur_date_ret_val=${ret_val}`);
    return ret_val;
}

function zero_first_format(value) {
    if (value < 10) {
        value = '0' + value;
    }
    return value;
}

function logOut() {
    xhr_req('GET', `${server_name}:${server_port}/${url_logout}`, req_logout);
}

function logIn(e) {
    const db_user = document.querySelector("#inputLogIn");
    const db_passwd = document.querySelector("#inputPassword");
    const db_new_password = document.querySelector("#inputNewPassword");
    const status = document.createElement('span');
    status.id = "logInSpinnerId";
    console.log("logIn.onclick");
    console.log(e);
    if (flagNewPassword.checked) {
        xhr_req('POST', `${server_name}:${server_port}/${url_change_password}`, req_login, `user=${db_user.value}&oldPass=${db_passwd.value}&newPass=${db_new_password.value}`);
    } else {
        xhr_req('POST', `${server_name}:${server_port}/${url_login}`, req_login, `user=${db_user.value}&pass=${db_passwd.value}`);
    }
    btnlogIn.disabled = true;
    status.className = "spinner-border spinner-border-sm";
    status.setAttribute("role", "status");
    document.querySelector("#logInModalLabel").append(status);
}

function req_login(err, data) {
    document.querySelector("#statusBar").innerHTML = "";
    document.getElementById("logInSpinnerId").remove();
    btnlogIn.disabled = false;
    if (err) {
        console.log('logIn error:Не верно указано имя пользователя или пароль!');
        document.querySelector("#statusBar").append(create_alert_element("Не верно указано имя пользователя или пароль!", "warning"));
    } else {
        document.querySelector("#statusBar").innerHTML = "";
        alertPlaceholder.innerHTML = "";
        WinLogInModal.hide();
        navbarUser.innerHTML = " " + document.querySelector("#inputLogIn").value;
        linkLogIn.classList.add("d-none");
        linkLogOut.classList.remove("d-none");
        navbarContent.classList.remove("d-none");
        console.log('login Ok!');
    };
}

function req_logout(err, data) {
    if (!err) {
        console.log('LogOut-Ok!')
        navbarUser.innerHTML = "";
        linkLogIn.classList.remove("d-none");
        linkLogOut.classList.add("d-none");
        navbarContent.classList.add("d-none");
    } else {
        // create_alert_element("соединение с базой данных отсутствует!")
        alert_output("отсутствует соединение с базой данных", "warning");
        console.log("соединение с базой данных отсутствует!");
    }
}


function create_alert_element(message, type) {
    const warn = document.createElement('div');
    warn.innerHTML = [
        `<div class="alert alert-${type} alert-dismissible" role="alert">`,
        `   <div>${message}</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('');
    return warn;
}

init();

