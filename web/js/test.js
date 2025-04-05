console.log('Begin ..');

//------------------------ Определение констант -------------------------
const url_login="http://msk-rss-appa0.megafon.ru:8765/rssvw/login";
const url_price="http://msk-rss-appa0.megafon.ru:8765/rssvw/price";
const url_rtpl_name="http://msk-rss-appa0.megafon.ru:8765/bis";
//------------------------ Определение переменных -------------------------
let btn_login = document.querySelector('#btn_login');
let btn_db_sign_in=document.querySelector('#btn_db_sign_in_id');
let db_user=document.querySelector('#db_user_id');
let db_passwd=document.querySelector('#db_passwd_id');
let btn_price=document.querySelector('#btn_price');
let btn_price_id=document.querySelector('#btn_price_id');
let btn_login_cancel=document.querySelector('#btn_login_cancel');
//------------------------ Определение обрабоки событий -------------------------
btn_login.onclick=show_login;
btn_db_sign_in.onclick=db_login;
btn_price.onclick=show_price;
btn_price_id.onclick=get_price;
btn_login_cancel.onclick=close_login;
//------------------------ Определение обрабоки событий -------------------------

function close_login() {
    show_login(false);
}

function rssvw_login() {
    let url_login = 'http://msk-rss-appa0.megafon.ru:8765/rssvw/login?user=bis&pass=HI8w6DnG7i%23qU5p';
    fetch(url_login, {
        method:'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
        .then((resp) => {
            console.log(resp);
        })
        .catch((err) => {
            console.log(err);
        })
}

function xhr_req(p_method,p_url,p_date,p_exec) {
    // const url_req='http://msk-rss-appa0.megafon.ru:8765/rssvw/login?user=bis&pass=HI8w6DnG7i%23qU5p';
    let xhr=new XMLHttpRequest();
    xhr.open(p_method,p_url,true);
    xhr.withCredentials=true;
    xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    if(p_method.toUpperCase()=='POST') {
        xhr.send(p_date);
    } else {
        xhr.send();
    }  

    xhr.onreadystatechange=p_exec;
}

function put_log() {
    if(this.readyState==4 && this.status==200) {
        // console.log("request state: Ok !!!")
        console.log(this.responseText);
    }
}

function show_form(elm,mode) {
    elm.style.display=mode==true?'block':'none';
}

function show_login() {
    show_form(login_form,true);
}

function db_login() {
     event.preventDefault();
    console.log('db_login_function..');
    console.log(db_user.value);
    console.log(db_passwd.value);
    xhr_req('POST',url_login,`user=${db_user.value}&pass=${db_passwd.value}`,put_log);
    show_form(login_form,false);
}

function show_price() {
    show_form(price_form,true);
}

function get_price() {
    event.preventDefault();
    let rtpl_id=document.querySelector('#rtpl_id').value;
    let start_date=document.querySelector('#start_date_id').value;
    console.log(`rtpl_id=${rtpl_id}`);
    console.log(`start_date=${start_date}`);
    xhr_req('GET',`${url_rtpl_name}/getRtplName/${rtpl_id}`,put_log);
    xhr_req('POST',url_price,`rtplid=${rtpl_id}&date=${start_date}`,put_log);
    // xhr_req('GET',`${url_rtpl_name}/rpdr/${rtpl_id}/${start_date}`,put_log);
    show_form(price_form,false);
}

console.log('end');
 