<template>


  <div class="container">
    <div class="operator-box">
      <a-button @click="uploadModal = true">
        <template #icon>
          <UploadOutlined />
        </template>
        上传成绩
      </a-button>
      <a-button @click="toRecord" style="margin-left: 0.5rem;">
        <template #icon>
          <FileSearchOutlined />
        </template>
          查看历史结果
      </a-button>
      <a-modal v-model:visible="uploadModal" title="上传文件" :confirm-loading="uploadLoading" @ok="handleOk"
        @before-upload="beforeUpload" okText="上传" :closable="false" :maskClosable="false" cancelText="取消">
        <a-upload-dragger id="uploadRef" v-model:fileList="fileList" name="files" :multiple="true"
          :before-upload="beforeUpload" @drop="handleDrop" @remove="handleRemove">
          <p class="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p class="ant-upload-text">点击或拖动文件上传</p>
          <p class="ant-upload-hint">
            支持多个文件一起上传
          </p>
        </a-upload-dragger>
      </a-modal>
    </div>

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
import { message } from 'ant-design-vue';
import { UploadOutlined, InboxOutlined,FileSearchOutlined} from '@ant-design/icons-vue';
import type { UploadChangeParam } from 'ant-design-vue';
import { Ref, defineComponent, ref } from 'vue';
import axios from 'axios';
import { RouterLink } from 'vue-router';
import router from '../router';

interface DataItem {
  name: string
  size: number
  date: string
}
export default defineComponent({
  components: {
    UploadOutlined,
    FileSearchOutlined,
    InboxOutlined,
    RouterLink
},
  setup() {
    const fileList = ref([])

    const dataSource: Ref<DataItem[]> = ref([]);

    let uploadModal = ref(false)

    const uploadLoading = ref<boolean>(false);

    const toRecord = () => {
      router.push("/record")
    }

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
        url: 'http://localhost:8080/api/get/input',
        method: 'GET',
      })
        .then((resp) => {
          console.log(resp.data)
          dataSource.value = resp.data
        })
    }
    getInput()

    const beforeUpload = () => {
      console.log("before")

      fileList.value = [...fileList.value]
      return false;
    };

    const handleRemove = (file: File) => {
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

      if (fileList.value.length == 0) return

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
          message.success('上传成功')
          uploadModal.value = false
          getInput()
        })
        .catch(() => {
          uploadLoading.value = false;
          message.error('上传失败');
        });
    }

    const uploadChange = (info: UploadChangeParam) => {
      const status = info.file.status;
      if (status !== 'uploading') {
        console.log(info.file, info.fileList);
      }
      if (status === 'done') {
        message.success(`${info.file.name} 上传成功`);
      } else if (status === 'error') {
        message.error(`${info.file.name} 上传失败`);
      }
    };

    const handleDrop = (e: DragEvent) => {
      console.log(e);
    }


    return {
      columns,
      dataSource,
      uploadChange,
      toRecord,
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

</style>
  
  