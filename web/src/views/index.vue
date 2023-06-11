<template>
    <div class="logo">MapReduce成绩分析</div>

    <div class="container">
        <div class="operator-box">
          <a-button @click="uploadModal = true">
            <template #icon>
              <UploadOutlined />
            </template>
            上传成绩
          </a-button>
          <a-modal
            v-model:visible="uploadModal"
            title="上传文件"
            :confirm-loading="uploadLoading"
            @ok="handleOk"
            @before-upload="beforeUpload"
            okText="上传"
            :closable="false"
            :maskClosable="false"
            cancelText="取消"
          >
            <a-upload-dragger
              id="uploadRef"
              v-model:fileList="fileList"
              action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
              name="files"
              :multiple="true"
              :before-upload="beforeUpload"
              @drop="handleDrop"
              @remove="handleRemove"
            >
              <p class="ant-upload-drag-icon">
                <InboxOutlined/>
              </p>
              <p class="ant-upload-text">点击或拖动文件上传</p>
              <p class="ant-upload-hint">
                支持多个文件一起上传
              </p>
            </a-upload-dragger>
          </a-modal>
        </div>
        
        <a-table bordered :pagination="{ pageSize : 6}" :data-source="dataSource" :columns="columns">
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'coPhoto'">
                    <img :src="record.coPhoto" alt="商品图片" style="width: 30px; height:30px;"/>
                </template>
                <template v-if="column.dataIndex === 'operation'">
                      <span style="margin: 15px;">
                        <a>详情</a>
                      </span>
                </template>
            </template>
        </a-table>
      
    </div>
</template>
  <script lang="ts">
 import { message } from 'ant-design-vue';
import { UploadOutlined,InboxOutlined } from '@ant-design/icons-vue';
import type { UploadChangeParam } from 'ant-design-vue';
import { Ref, defineComponent, ref } from 'vue';
import axios from 'axios';

interface DataItem {
  filename:string
  date:string
}
  export default defineComponent({
    components: {
      UploadOutlined,
      InboxOutlined,
    },
    setup() {
        const fileList = ref([])

        const dataSource: Ref<DataItem[]> = ref([]);

        let uploadModal = ref(false)

        const uploadLoading = ref<boolean>(false);

        const columns = [
          {
              title: '成绩文件',
              dataIndex: 'filename',
          },
          {
              title: '上传时间',
              dataIndex: 'date',
          },
          {
              title: '分析结果',
              dataIndex: 'date',
          },
          {
              title:'操作',
              dataIndex:'operation',
          },
      ];
      
      const beforeUpload = () => {
        console.log("before")
        
        fileList.value = [...fileList.value]
        return false;
      };
      
      const handleRemove= (file: File) => {
        console.log("removing")
        if (fileList.value == null || fileList.value.length == 0) {
            return
        }
        const index = fileList.value.indexOf(file);
        const newFileList = fileList.value.slice();
        newFileList.splice(index, 1);
        fileList.value = newFileList;   
     };

      const handleOk = () => {
        
        if(fileList.value.length ==0) return
        
        const formData = new FormData()
        uploadLoading.value = true;

        for (let i of fileList.value) {
          formData.append('files', i.originFileObj)
        }


        axios('http://localhost:8080/api/upload', {
          method: 'POST',
          data: formData,
        })
          .then(() => {
            fileList.value = [];
            uploadLoading.value = false;
            message.success('upload successfully.');
            uploadModal.value=false
          })
          .catch(() => {
            uploadLoading.value = false;
            message.error('upload failed.');
          });
      }

      const uploadChange = (info: UploadChangeParam) => {
        const status = info.file.status;
        if (status !== 'uploading') {
          console.log(info.file, info.fileList);
        }
        if (status === 'done') {
          message.success(`${info.file.name} file uploaded successfully.`);
        } else if (status === 'error') {
          message.error(`${info.file.name} file upload failed.`);
        }
    };

      const handleDrop =  (e: DragEvent) => {
        console.log(e);
      }


      return {
        columns,
        dataSource,
        uploadChange,
        uploadLoading,
        handleRemove,
        beforeUpload,
        uploadModal,
        handleOk,
        handleDrop,
        fileList,
      };
    },
  });
  </script>
  
  <style scoped>
    .operator-box {
        margin-bottom: 1rem;
    }
    .container {
        padding: 2rem;
    }
    .logo {
        height: 4rem;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        font-weight: 900;
        box-shadow: 10px 5px 5px rgb(213, 211, 211);
        @supports (-webkit-background-clip: text) or (background-clip: text) {
            background-image: linear-gradient(to right, #eea2a2 0%, #bbc1bf 19%, #57c6e1 42%, #b49fda 79%, #7ac5d8 100%);
            -webkit-background-clip:text;
            -webkit-text-fill-color:transparent;
        }   
    }
  </style>
  
  