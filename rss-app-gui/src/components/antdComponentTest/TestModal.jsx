import React, { useState } from 'react';
import { Button, Modal } from "antd";

const TestModal = function () {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const showModal = () => {
    setOpen(true);
  };
  const handleOk = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      setOpen(false);
    }, 3000);
  };
  const handleCancel = () => {
    setOpen(false);
  };
  return (
    <>
      <Button type="primary" onClick={showModal}>Open Modal</Button>
      <Modal
        title="Title modal costomized footer" 
        // style={{
        //   top:20,
        // }}
        centered
        // whidth={1000}
        cancelText="Отмена"
        open={open}
        onOk={handleOk}
        onCancel={handleCancel}
        footer={[
          <Button key="back" onClick={handleCancel}>
            Return
          </Button>,
          <Button key="submit" type="primary" loading={loading} onClick={handleOk}>
            Submit
          </Button>,
          // <Button
          //   key="link"
          //   href="http://msk-rss-appa0:8443/rss/ocsdb/get-testData"
          //   type="primary"
          //   loading={loading}
          //   onClick={handleOk}
          // >
          //   Fetch on OCSDB
          // </Button>
        ]}
      >
        <p>Some content..</p>
        <p>Some content..</p>
        <p>Some content..</p>
        <p>Some content..</p>
        <p>Some content..</p>
      </Modal>
    </>)
}

export default TestModal;