console.log('Begin ..');

//------------------------ Определение констант -------------------------
const server_name = "http://msk-rss-appa0.megafon.ru";
const server_port = "8400";
const url_login = "rssvw/login";
const url_logout = "rssvw/logout";
const url_trf_price = "rssvw/trafficprice";
const url_rtpl_name = "rssvw/rpname";
//------------------------ Определение переменных -------------------------
let btn_login = document.querySelector('#btn_login');
let btn_logout = document.querySelector('#btn_logout');
let btn_db_sign_in = document.querySelector('#btn_db_sign_in_id');
let db_user = document.querySelector('#db_user_id');
let db_passwd = document.querySelector('#db_passwd_id');
let btn_price = document.querySelector('#btn_price');
let btn_price_id = document.querySelector('#btn_price_id');
let btn_login_cancel = document.querySelector('#btn_login_cancel');
let tbl_tariff_id = document.querySelector("#tbl-tariff-id");
let btn_frm_tariff_close = document.querySelector("#btn-frm-tariff-close");
let frm_tariff = document.querySelector("#frm-tariff-id");
let frm_price = document.querySelector("#price_form");
let btn_price_cancel = document.querySelector("#btn_price_cancel_id");
let radio_rtpl_id = document.querySelector("#radio_rtpl_id");
let radio_rtpl_name = document.querySelector("#radio_rtpl_name_id");
let radio_tap_code = document.querySelector("#radio_tap_code_id");
//------------------------ Определение обрабоки событий -------------------------
btn_login.onclick = show_login;
btn_logout.onclick = db_logout;
btn_db_sign_in.onclick = db_login;
btn_price.onclick = show_price;
// btn_price_id.onclick = get_price;
btn_price_id.onclick = get_trf_price;
btn_login_cancel.onclick = disable_login;
btn_frm_tariff_close.onclick = frm_tariff_del;
btn_price_cancel.onclick = disable_price_form;
//------------------------ Определение обрабоки событий -------------------------

radio_rtpl_id.onclick = function () {
    document.querySelector('#rtpl_id').value = "";
    document.querySelector("#lbl_rtpl_id").innerHTML = "rtpl_id";
}

radio_rtpl_name.onclick = function () {
    document.querySelector('#rtpl_id').value = "";
    document.querySelector("#lbl_rtpl_id").innerHTML = "rtpl_name";
};

radio_tap_code.onclick = function () {
    document.querySelector('#rtpl_id').value = "";
    document.querySelector("#lbl_rtpl_id").innerHTML = "tap_code";
};

function show_form(elm, mode) {
    elm.style.display = mode == true ? 'block' : 'none';
}

function show_login() {
    console.log('show_login..');
    document.getElementById("db_user_id").innerHTML="";
    document.getElementById("db_passwd_id").innerHTML="";
    show_form(login_form, true);
}

function show_price() {
    document.querySelector("#lbl_rtpl_id").innerHTML = "rtpl_name";
    document.querySelector('#rtpl_id').value = "";
    document.querySelector('#start_date_id').value = cur_date();
    document.getElementById("trf_req_status").style.color="black";
    document.getElementById("trf_req_status").innerHTML="";
    btn_price.disabled = true;
    btn_logout.disabled = true;
    show_form(price_form, true);
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

function disable_login(event) {
    event.preventDefault();
    document.getElementById("db_user_id").innerHTML="";
    document.getElementById("db_passwd_id").innerHTML="";
    show_form(login_form,false);
};

function disable_price_form(event) {
    event.preventDefault();
    btn_price.disabled = false;
    btn_logout.disabled = false;
    show_form(frm_price, false);
}

function xhr_req(p_method, p_url, p_exec, p_data) {
    let xhr = new XMLHttpRequest();
    xhr.open(p_method, p_url, true);
    // xhr.withCredentials = true;
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    if (p_method.toUpperCase() == 'POST') {
        xhr.send(p_data);
    } else {
        xhr.send();
    }

    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                p_exec(null, this.response);
            } else {
                console.log('Error');
                p_exec('err', this.response);
            }
        }
    }
}

function db_login(event) {
    event.preventDefault();
    console.log('db_login_function..');
    console.log(db_user.value);
    console.log(db_passwd.value);
    document.getElementById("log_status").innerHTML = '';
    xhr_req('POST', `${server_name}:${server_port}/${url_login}`, req_login, `user=${db_user.value}&pass=${db_passwd.value}`);
    document.getElementById("log_status").style.color = "green";
    document.getElementById("log_status").innerHTML = ' Waiting..';
    // show_form(login_form, false);
}

function req_login(err, data) {
    if (err) {
        console.log('Login incorrect!');
        document.getElementById("log_status").style.color = "red";
        document.getElementById("log_status").innerHTML = 'login incorrect!';
    } else {
        console.log('Login Ok!');
        btn_login.disabled = true;
        btn_logout.disabled = false;
        btn_price.disabled = false;
        document.getElementById("log_status").innerHTML = '';
        show_form(login_form, false);
    };
}

function db_logout(event) {
    event.preventDefault();
    xhr_req('GET', `${server_name}:${server_port}/${url_logout}`, req_logout);
}

function req_logout(err, data) {
    if (!err) {
        btn_login.disabled = false;
        btn_price.disabled = true;
        btn_logout.disabled = true;
    }
}

function get_trf_price(event) {
    event.preventDefault();
    let rtpl_id = document.querySelector('#rtpl_id').value;
    let start_date = document.querySelector('#start_date_id').value;
    let tp = document.querySelectorAll('input[name="inp_type"]');
    val_type = "id";
    console.log(tp);
    for (let i = 0; i < tp.length; i++) {
        if (tp[i].checked) {
            val_type = tp[i].value;
        }
    }
    console.log(`rtpl_id=${rtpl_id}`);
    console.log(`start_date=${start_date}`);
    xhr_req('POST', `${server_name}:${server_port}/${url_trf_price}`, put_tarif, `val_tp=${val_type}&value=${rtpl_id}&date=${start_date}`);
    document.getElementById("trf_req_status").style.color="green";
    document.getElementById("trf_req_status").innerHTML="Waiting";
}

function put_tarif(err, data) {
    if (!err) {
         show_form(price_form, false);
        console.log(data);
        let start_date = document.querySelector('#start_date_id').value;
        const t_body = document.createElement("tbody");
        t_body.setAttribute("id", "tbl_tarif_body_id");
        data = JSON.parse(data);
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
        tbl_tariff_id.appendChild(t_body);
        xhr_req('GET', `${server_name}:${server_port}/${url_rtpl_name}/${data[0].rtplId}?&date=${start_date}`, getRPName);
        show_form(frm_tariff, true);
    } else {
        document.getElementById("trf_req_status").style.color="red";
        document.getElementById("trf_req_status").innerHTML="Error request";
        console.log('Тарифный план не найден!');
    }
}

function add_tbl_price_ceil(row, val) {
    const ceil = document.createElement("td");
    ceil.appendChild(val);
    ceil.classList.add("tbl-price-th");
    row.appendChild(ceil);
}

function getRPName(err, data) {
    console.log(data);
    document.querySelector('#tbl-price-head').innerHTML = `Тарифный план:${data}`;
}

function frm_tariff_del(event) {
    event.preventDefault();
    t_body = document.querySelector("#tbl_tarif_body_id");
    tbl_tariff_id.removeChild(t_body);
    btn_price.disabled = false;
    btn_logout.disabled = false;
    // btn_price.disabled = false;
    // btn_logout.disabled = false;
    show_form(frm_tariff, false);
}

//---------------------инициализация элементов-------------------------------
btn_price.disabled = true;
btn_logout.disabled = true;
console.log('end');