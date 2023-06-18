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
      <a-input v-model:value="studentName" placeholder="输入学生姓名" style="width: 20em; margin-left: 35rem" />
      <a-select ref="select" v-model:value="caulMethod" :options="options" style="width: 8em;margin-left: .5rem;" />
      <a-button style="margin-left: 0.5rem;" :loading="loading" @click="calculate()">
        <template #icon>
          <ApiOutlined />
        </template>
        计算
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

    <a-table bordered :pagination="{ pageSize: 6 }" :data-source="dataSource" :columns="columns"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }">
    </a-table>

    <a-modal v-model:visible="modalVisible" title="分析结果" @ok="modalVisible = false">
      <a-card style="text-align: center;">
        <div class="record-content" style="height: 30vh; overflow: hidden auto;">
          <h4 v-for="item in content" style="text-align: left;">
            {{ item }}
          </h4>
        </div>
      </a-card>
    </a-modal>

  </div>
</template>
<script lang="ts">
import { message } from 'ant-design-vue';
import { UploadOutlined, InboxOutlined, ApiOutlined, FileSearchOutlined } from '@ant-design/icons-vue';
import type { UploadChangeParam, SelectProps } from 'ant-design-vue';
import { Ref, defineComponent, ref, reactive, toRefs } from 'vue';
import axios from 'axios';
import { RouterLink } from 'vue-router';
import router from '../router';

type Key = string | number;

interface DataItem {
  key: Key
  name: string
  size: number
  date: string
}
export default defineComponent({
  components: {
    UploadOutlined,
    FileSearchOutlined,
    ApiOutlined,
    InboxOutlined,
    RouterLink
  },
  setup() {

    const studentName = ref<string>('');

    const fileList = ref([])

    const dataSource: Ref<DataItem[]> = ref([]);

    let uploadModal = ref(false)
    let caulMethod = ref('计算平均值')
    const uploadLoading = ref<boolean>(false);

    const toRecord = () => {
      router.push("/record")
    }

    const options = ref<SelectProps['options']>([
      {
        value: '计算平均值',
        label: '计算平均值',
      },
      {
        value: '计算最高分',
        label: '计算最高分',
      },
      {
        value: '计算最低分',
        label: '计算最低分',
      },
      {
        value: '统计选修人数',
        label: '统计选修人数',
      },
      {
        value: '查找学生',
        label: '查找学生',
      },
      {
        value: '统计区间',
        label: '统计区间',
      },
      {
        value: '统计各个分数人数',
        label: '统计各个分数人数',
      }
    ]);

    const state = reactive<{
      selectedRowKeys: string[];
      loading: boolean;
    }>({
      selectedRowKeys: [],
      loading: false,
    });

    const onSelectChange = (selectedRowKeys: string[]) => {
      state.selectedRowKeys = selectedRowKeys;
    };

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
    ];

    const getInput = () => {
      axios({
        url: 'http://localhost:8080/api/get/input',
        method: 'GET',
      })
        .then((resp) => {
          dataSource.value = resp.data
          for (let i of dataSource.value) {
            i.key = i.name
          }
        })
    }
    getInput()

    const beforeUpload = () => {
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

    //计算
    let modalVisible = ref(false)
    let content: Ref<string[]> = ref([])
    const calculate = () => {
      if (state.selectedRowKeys.length == 0) {
        message.warn("请选择文件!");
      } else {
        if (caulMethod.value === "计算平均值") {
          console.log(state.selectedRowKeys.toString())
          state.loading = true;
          axios({
            url: 'http://localhost:8080/api/average/map',
            method: 'POST',
            params: {
              files: state.selectedRowKeys.toString(),
              desc: true
            }
          })
            .then((resp) => {
              if (resp.data.error_info === "success") {
                message.success('计算成功');
                axios({
                  url: 'http://localhost:8080/api/record/content',
                  method: 'GET',
                  params: {
                    fileName: resp.data.filePath
                  }
                }).then((resp) => {
                  modalVisible.value = true
                  let data: string = resp.data;
                  content.value = data.split("\n")
                })
              } else {
                message.error(resp.data.error_info);
              }
              setTimeout(() => {
                state.loading = false;
                state.selectedRowKeys = [];
              }, 1000);
            })
        } else if (caulMethod.value === "计算最高分") {
          state.loading = true;
          axios({
            url: 'http://localhost:8080/api/max/map',
            method: 'POST',
            params: {
              files: state.selectedRowKeys.toString(),
            }
          })
            .then((resp) => {
              if (resp.data.error_info === "success") {
                message.success('计算成功');
                axios({
                  url: 'http://localhost:8080/api/record/content',
                  method: 'GET',
                  params: {
                    fileName: resp.data.filePath
                  }
                }).then((resp) => {
                  modalVisible.value = true
                  let data: string = resp.data;
                  content.value = data.split("\n")
                })
              } else {
                message.error(resp.data.error_info);
              }
              setTimeout(() => {
                state.loading = false;
                state.selectedRowKeys = [];
              }, 1000);
            })
        } else if (caulMethod.value === "计算最低分") {
          state.loading = true;
          axios({
            url: 'http://localhost:8080/api/min/map',
            method: 'POST',
            params: {
              files: state.selectedRowKeys.toString(),
            }
          })
            .then((resp) => {
              if (resp.data.error_info === "success") {
                message.success('计算成功');
                axios({
                  url: 'http://localhost:8080/api/record/content',
                  method: 'GET',
                  params: {
                    fileName: resp.data.filePath
                  }
                }).then((resp) => {
                  modalVisible.value = true
                  let data: string = resp.data;
                  content.value = data.split("\n")
                })
              } else {
                message.error(resp.data.error_info);
              }
              setTimeout(() => {
                state.loading = false;
                state.selectedRowKeys = [];
              }, 1000);
            })
        } else if (caulMethod.value === "统计选修人数") {
          state.loading = true;
          axios({
            url: 'http://localhost:8080/api/countPeople/map',
            method: 'POST',
            params: {
              files: state.selectedRowKeys.toString(),
            }
          })
            .then((resp) => {
              if (resp.data.error_info === "success") {
                message.success('计算成功');
                axios({
                  url: 'http://localhost:8080/api/record/content',
                  method: 'GET',
                  params: {
                    fileName: resp.data.filePath
                  }
                }).then((resp) => {
                  modalVisible.value = true
                  let data: string = resp.data;
                  content.value = data.split("\n")
                })
              } else {
                message.error(resp.data.error_info);
              }
              setTimeout(() => {
                state.loading = false;
                state.selectedRowKeys = [];
              }, 1000);
            })
        } else if (caulMethod.value === "查找学生") {
          if (studentName.value === "") {
            message.warn("请输入学生姓名!");
          } else {
            state.loading = true;
            axios({
              url: 'http://localhost:8080/api/findStudent/map',
              method: 'POST',
              params: {
                files: state.selectedRowKeys.toString(),
                student: studentName.value
              }
            })
              .then((resp) => {
                if (resp.data.error_info === "success") {
                  message.success('查找成功');
                  axios({
                    url: 'http://localhost:8080/api/record/content',
                    method: 'GET',
                    params: {
                      fileName: resp.data.filePath
                    }
                  }).then((resp) => {
                    modalVisible.value = true
                    let data: string = resp.data;
                    content.value = data.split("\n")
                  })
                } else {
                  message.error(resp.data.error_info);
                }
                setTimeout(() => {
                  state.loading = false;
                  state.selectedRowKeys = [];
                }, 1000);
              })
          }
        } else if (caulMethod.value === "统计区间") {
          state.loading = true;
          axios({
            url: 'http://localhost:8080/api/statisticInterval/map',
            method: 'POST',
            params: {
              files: state.selectedRowKeys.toString(),
            }
          })
            .then((resp) => {
              if (resp.data.error_info === "success") {
                message.success('计算成功');
                axios({
                  url: 'http://localhost:8080/api/record/content',
                  method: 'GET',
                  params: {
                    fileName: resp.data.filePath
                  }
                }).then((resp) => {
                  modalVisible.value = true
                  let data: string = resp.data;
                  content.value = data.split("\n")
                })
              } else {
                message.error(resp.data.error_info);
              }
              setTimeout(() => {
                state.loading = false;
                state.selectedRowKeys = [];
              }, 1000);
            })
        } else if (caulMethod.value === "统计各个分数人数") {
          state.loading = true;
          axios({
            url: 'http://localhost:8080/api/samePeople/map',
            method: 'POST',
            params: {
              files: state.selectedRowKeys.toString(),
            }
          })
            .then((resp) => {
              if (resp.data.error_info === "success") {
                message.success('计算成功');
                axios({
                  url: 'http://localhost:8080/api/record/content',
                  method: 'GET',
                  params: {
                    fileName: resp.data.filePath
                  }
                }).then((resp) => {
                  modalVisible.value = true
                  let data: string = resp.data;
                  content.value = data.split("\n")
                })
              } else {
                message.error(resp.data.error_info);
              }
              setTimeout(() => {
                state.loading = false;
                state.selectedRowKeys = [];
              }, 1000);
            })
        }
      }
    }

    return {
      modalVisible,
      content,
      studentName,
      calculate,
      onSelectChange,
      ...toRefs(state),
      caulMethod,
      columns,
      dataSource,
      uploadChange,
      toRecord,
      uploadLoading,
      options,
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
  
  