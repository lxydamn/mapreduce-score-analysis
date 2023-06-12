<template>


    <div class="container">
      <a-table bordered :pagination="{ pageSize: 6 }" :data-source="dataSource" :columns="columns">
        <template #bodyCell="{ column }">
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
  import { UploadOutlined, InboxOutlined } from '@ant-design/icons-vue';
  import { Ref, defineComponent, ref } from 'vue';
  import axios from 'axios';
  
  interface DataItem {
    name: string
    size: number
    date: string
  }
  export default defineComponent({
    components: {
      UploadOutlined,
      InboxOutlined,
    },
    setup() {
  
      const dataSource: Ref<DataItem[]> = ref([]);
  
      const columns = [
        {
          title: '成绩文件',
          dataIndex: 'name',
        },
        {
          title: '文件大小(B)',
          dataIndex: 'size',
        },
        {
          title: '上传时间',
          dataIndex: 'date',
        },
        {
          title: '操作',
          dataIndex: 'operation',
        },
      ];
  
      const getInput = () => {
        axios({
          url: 'http://localhost:8080/api/get/record',
          method: 'GET',
        })
          .then((resp) => {
            console.log(resp.data)
            dataSource.value = resp.data
          })
      }
      getInput()
  
    
  
      return {
        dataSource,
        columns,
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
  
  </style>
    
    