import React, { useState } from "react";

const Counter = function() {
    const [count, changeCount] = useState(0)

    function increment() {
        changeCount(count + 1)
    }

    function decrement() {
        changeCount(count - 1)
    }

        return (
            <div>
                <h1>{count}</h1>
                <button onClick={increment}>Increment</button>
                <button onClick={decrement}>Decrement</button>
            </div>
        )
    }

    export default Counter;