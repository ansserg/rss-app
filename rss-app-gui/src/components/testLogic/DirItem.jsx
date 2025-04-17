import React from "react";


const DirItem=function(props) {

    return(
        <div>name={props.elem.dirName},price={props.elem.dirPrice}</div>
    )
}

export default DirItem;