import React from 'react';
import { createRoot } from "react-dom/client"
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./store/store"
import App from './containers/app';

const rootElement = document.getElementById("root");
const root = createRoot(rootElement);

root.render(
    // <Provider store={store}>
    //     <BrowserRouter>
    //         <App />
    //     </BrowserRouter>,
    // </Provider>
)


