import React, { useState } from "react";

const DirPriceForm=function(props) {
    const [dir, setDir] = useState("");
    const [price, setPrice] = useState("");

    const addPriceDirHandler = (event) => {
        event.preventDefault();
        const elem = {
          id: props.idSeq,
          dirName: dir,
          dirPrice: price
        }
        props.creDirPrice(elem);
        console.log("dir=" + dir);
        console.log("price=" + price);
        setDir("");
        setPrice("");
      }

    return(
        <form>
        <input
          type='text'
          placeholder='Наименование направления'
          value={dir}
          onChange={e => setDir(e.target.value)}
        />
        <input
          type='text'
          placeholder='Тариф,$'
          value={price}
          onChange={e => setPrice(e.target.value)}
        />
        <button onClick={addPriceDirHandler}>Добавить</button>
      </form>
    )
}

export default DirPriceForm;