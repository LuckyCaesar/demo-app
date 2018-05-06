<html>
<head>
    <title>Data Analysis</title>
    <link type="text/css" href="/css/bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="/css/bootstrap-grid.css" rel="stylesheet"/>
    <link type="text/css" href="/css/bootstrap-reboot.css" rel="stylesheet"/>
    <style>
        .title {
            color: #8b0000;
        }
        .btn-self {
            width: 100px;
            height: 35px;
            border: 1px;
            border-radius: 4px;
            margin: 8px 8px;
        }
        .block {
            margin: 20px 20px;
        }
        ul.pagination {
            display: inline-block;
            padding: 0;
            margin: 0;
        }
        ul.pagination li {
            display: inline;
        }
        ul.pagination li a {
            cursor: pointer;
            color: black;
            float: left;
            padding: 8px 16px;
            text-decoration: none;
            transition: background-color .3s;
            border: 1px solid #ddd;
        }
        ul.pagination li a.active {
            background-color: #4CAF50;
            color: white;
        }
        ul.pagination li a:hover:not(.active) {
            background-color: #ddd;
        }
        hr {
            border: 0;
            border-bottom: 2px solid black;
        }
        table {
            word-break: break-all;
        }
    </style>
</head>
<body>
    <div class="view">
        <div class="block">
            <h2 class="title">上传文件</h2>
            <div>
                <input type="file" @change="filePreview($event)">
                <button type="button" class="btn-success btn-self" @click="uploadFile">上传</button>
            </div>
        </div>
        <hr/>
        <div class="block">
            <h2 class="title">数据列表</h2>
            <div>
                <button type="button" class="btn-success btn-self" @click="search">刷新</button>
                <table class="table table-bordered table-hover">
                    <thead>
                        <th>A1</th>
                        <th>A2</th>
                        <th>A3</th>
                        <th>A4</th>
                        <th>A5</th>
                        <th>A6</th>
                        <th>A7</th>
                        <th>A8</th>
                        <th>A9</th>
                        <th>A10</th>
                        <th>A11</th>
                        <th>A12</th>
                        <th>quality</th>
                    </thead>
                    <tbody>
                    <template v-for="data in dataList">
                        <tr>
                            <td>{{data.columnA}}</td>
                            <td>{{data.columnB}}</td>
                            <td>{{data.columnC}}</td>
                            <td>{{data.columnD}}</td>
                            <td>{{data.columnE}}</td>
                            <td>{{data.columnF}}</td>
                            <td>{{data.columnG}}</td>
                            <td>{{data.columnH}}</td>
                            <td>{{data.columnI}}</td>
                            <td>{{data.columnJ}}</td>
                            <td>{{data.columnK}}</td>
                            <td>{{data.columnL}}</td>
                            <td>{{data.quality}}</td>
                        </tr>
                    </template>
                    </tbody>
                </table>
            </div>
            <page-bar :search-params="queryParam" :page="page" :search="getDatas"/>
        </div>
        <hr/>
        <div class="block">
            <h2 class="title">数据分析</h2>
            <div>
                <table class="table table-bordered table-hover">
                    <thead>
                        <th></th>
                        <th>A1</th>
                        <th>A2</th>
                        <th>A3</th>
                        <th>A4</th>
                        <th>A5</th>
                        <th>A6</th>
                        <th>A7</th>
                        <th>A8</th>
                        <th>A9</th>
                        <th>A10</th>
                        <th>A11</th>
                        <th>A12</th>
                        <th colspan="2">quality</th>
                    </thead>
                    <tbody>
                    <template v-for="line in 4">
                        <tr>
                            <template v-if="line === 1">
                                <td>平均值</td>
                                <template v-for="result in dataAnalysisResults">
                                    <td>{{result.averageValue}}</td>
                                </template>
                            </template>
                            <template v-if="line === 2">
                                <td>标准差</td>
                                <template v-for="result in dataAnalysisResults">
                                    <td>{{result.standardDeviation}}</td>
                                </template>
                            </template>
                            <template v-if="line === 3">
                                <td>离群值个数</td>
                                <template v-for="result in dataAnalysisResults">
                                    <td>{{result.outlierNum}}</td>
                                </template>
                            </template>
                            <template v-if="line === 4">
                                <td>因子数</td>
                                <template v-for="result in dataAnalysisResults">
                                    <td v-if="result.factorInfo === null"></td>
                                    <td v-else>
                                        ok : {{result.factorInfo.ok}}
                                        bad : {{result.factorInfo.bad}}
                                    </td>
                                </template>
                            </template>
                        </tr>
                    </template>
                    </tbody>
                </table>
            </div>
        </div>
        <hr/>
        <div class="block">
            <h2 class="title">下载文件</h2>
            <div class="download-block">
                <form action="/app/data/downloadFile" method="post" accept-charset="UTF-8">
                    <button type="submit" class="btn-success btn-self">点击下载</button>
                </form>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript" src="/js/vue2.0.js"></script>
<script type="text/javascript" src="/js/sweetalert2.all.js"></script>
<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/js/axios.js"></script>
<script type="text/javascript" src="/index_load.js"></script>
</html>