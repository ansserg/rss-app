function setrtplparam(mode) {
    document.getElementById('setrtplmodeid').value=mode;
    document.getElementById("taskid").value=document.getElementById("rfcid").innerHTML;
    document.getElementById("fnameid").value=document.getElementById("filenameid").innerHTML;
}

function sendPriceList(mode) {
    price_file.onsubmit = async (e) => {
        e.preventDefault();
        let formData = new FormData();
        formData.append("file",document.getElementById("price_file").files[0]);
        formData.append("task",document.getElementById("price_task").value);
        formData.append("mode",mode);
        let response = await fetch("/rtpl/upload_file", {
            method: "POST",
            body: formData
        });
    }
}