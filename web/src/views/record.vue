<template>
  <div class="container">
    <a-table bordered :pagination="{ pageSize: 6 }" :data-source="dataSource" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <span style="margin: 15px;">
            <a @click="openRecord(record)">详情</a>
          </span>
        </template>
      </template>
    </a-table>
  </div>

  <a-modal v-model:visible="modalVisible" title="分析详情" @ok="modalVisible = false">
    <a-card style="text-align: center;">
      <h4 v-for="item in content" style="text-align: left;">
      {{ item }}
    </h4>
    </a-card>
  </a-modal>

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
    let modalVisible = ref(false)
    let content : Ref<string[]> = ref([])
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
      }).then((resp) => {
        dataSource.value = resp.data
      })
    }
    getInput()

    const openRecord = (record: any) => {
      console.log(record)
      axios({
        url: 'http://localhost:8080/api/record/content',
        method: 'GET',
        params: {
          fileName: record.name
        }
      }).then((resp) => {
        modalVisible.value = true     
        let data:string = resp.data;
        content.value = data.split("\n")
      })
    }


    return {
      content,
      dataSource,
      modalVisible,
      columns,
      openRecord,
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
    
    