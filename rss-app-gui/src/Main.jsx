import React from "react";
import { Provider } from "react-redux";
import store from "./store/store";
import { BrowserRouter } from "react-router-dom";
import App from "./containers/app";

const Main = function() {
    return (
        <Provider store={store} style={{background: "red"}}>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </Provider>
    )
}
export default Main;