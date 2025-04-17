import React, { useState } from "react";
import "../css/App.css";

function InputText(props) {
    const [valText, setValue] = useState(props.text);
    return (
        <div>
            <input
                type="text"
                value={valText}
                onChange={e => setValue(e.target.value)} />
            <span className="inpVal"> {valText}</span>
        </div>
    );
}

export default InputText;