<template>
    <div class="logo">MapReduce成绩分析</div>

    <div class="container">
        <div class="operator-box">
            <a-upload
                v-model:file-list="fileList"
                name="file"
                action="http://localhost:8080/api/upload"
                @change="handleChange"
            >
                <a-button>
                <upload-outlined></upload-outlined>
                上传成绩
                </a-button>
            </a-upload>
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
import { UploadOutlined } from '@ant-design/icons-vue';
import type { UploadChangeParam } from 'ant-design-vue';
import { Ref, defineComponent, ref } from 'vue';

  interface DataItem {
    filename:string
    date:string
  }
  export default defineComponent({
    components: {

    },
    setup() {
        const fileList = ref([])
        const dataSource: Ref<DataItem[]> = ref([]);
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
      const handleChange = (info: UploadChangeParam) => {
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
        fileList.value = []
      }
      if (info.file.status === 'done') {
        message.success(`${info.file.name} file uploaded successfully`);
        fileList.value = []
      } else if (info.file.status === 'error') {
        message.error(`${info.file.name} file upload failed.`);
        fileList.value = []
      }
      
    };

      return {
        columns,
        dataSource,
        handleChange,
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
  
  