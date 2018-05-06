Vue.prototype.$http = axios;

Vue.component("page-bar", {
    props: {
        searchParams: {
            type: Object,
            required: true
        },
        page: {
            type: Object,
            required: true
        },
        search: {
            type: Function,
            required: true
        }
    },
    methods: {
        jump: function () {
            this.search();
            window.scrollTo(0, 50);
        }
    },
    template: `
        <div>
            <ul class="pagination">
                <li v-if="1 >= page.currentPage" class="disabled">
                    <a title="Top Page">
                        首页
                    </a>
                </li>
                <li v-else>
                    <a title="Top Page" @click.prevent="searchParams.currentPage = 1, jump()">
                        首页
                    </a>
                </li>
                <li v-if="1 >= page.currentPage" class="disabled">
                    <a title="Prev">
                        上一页
                    </a>
                </li>
                <li v-else class="">
                    <a title="Prev" @click.prevent="searchParams.currentPage = (searchParams.currentPage - 1), jump()">
                        上一页
                    </a>
                </li>
                <template v-for="pageNo in page.totalPage">
                    <template v-if="pageNo >= (page.currentPage - 2) && (page.currentPage + 2) >= pageNo">
                        <li>
                            <a :class="{ active : pageNo == page.currentPage }"
                               @click.prevent="searchParams.currentPage = parseInt($event.target.text), jump()">
                                {{ pageNo }}
                            </a>
                        </li>
                    </template>
                </template>
                <li v-if="page.currentPage + 1 > page.totalPage" class="disabled">
                    <a @click.prevent title="Next">
                        下一页
                    </a>
                </li>
                <li v-else>
                    <a @click.prevent="searchParams.currentPage = (searchParams.currentPage + 1), jump()" title="Next">
                        下一页
                    </a>
                </li>
                <li v-if="searchParams.currentPage >= page.totalPage" class="disabled">
                    <a title="End Page">
                        尾页
                    </a>
                </li>
                <li v-else>
                    <a @click.prevent="searchParams.currentPage = page.totalPage, jump()" title="End Page">
                        尾页
                    </a>
                </li>
                <li class="disabled"><a>共 {{ page.totalPage }} 页</a></li>
                <li class="disabled"><a>共 {{ page.totalSize }} 条</a></li>
            </ul>
        </div>`
});

const vDataView = new Vue({
    el: '.view',
    data: {
        dataList: [],
        dataAnalysisResults: [],
        queryParam: {
            currentPage: 1
        },
        dataFile: '',
        page: {}
    },
    methods: {
        getDatas() {
            let self = this;
            swal("", "正在加载, 请稍候...", "info");
            swal.showLoading();
            $.ajax({
                url: '/app/data/getDatas',
                method: 'GET',
                data: self.queryParam,
                success: (result) => {
                    swal.close();
                    if (result.code === 1) {
                        self.dataList = result.data;
                        self.page = result.page;
                    } else {
                        swal("失败", result.message, "error");
                    }
                }
            })
        },
        getDataAnalysisResults() {
            let self = this;
            $.ajax({
                url: '/app/data/getDataAnalysisResults',
                method: 'GET',
                success: (result) => {
                    if (result.code === 1) {
                        self.dataAnalysisResults = result.data;
                    } else {
                        swal("失败", result.message, "error");
                    }
                }
            })
        },
        search() {
            this.queryParam.currentPage = 1;
            this.getDatas();
        },
        filePreview(e) {
            let files = e.target.files || e.dataTransfer.files;
            if (!files.length) return;
            this.dataFile = e.target.files[0];
        },
        uploadFile() {
            let self = this;
            let formData = new FormData();
            formData.append('dataFile', self.dataFile);

            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            };

            swal("", "正在上传, 请稍候...", "info");
            swal.showLoading();

            self.$http.post('/app/data/upload', formData, config).then((res) => {
                if (res.status === 200) {
                    let data = res.data;
                    if (data.code === 1) {
                        swal("成功", "上传成功", "success").then(() => {
                            self.getDatas();
                            self.getDataAnalysisResults();
                        });
                    } else {
                        swal("失败", data.message, "error");
                    }
                } else {
                    swal("失败", res.statusText, "error");
                }
            }).catch(response => {
                swal("失败", response.message, "error");
            });
        }
    },
    mounted() {
        this.getDatas();
        this.getDataAnalysisResults();
    }
});