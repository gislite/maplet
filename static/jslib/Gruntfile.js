// 包装函数
module.exports = function (grunt) {

    // 任务配置,所有插件的配置信息
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),


        // uglify插件的配置信息
        uglify: {
            options: {mangle: false,},
            build: {
                src: ['src/gscript.js', 'src/mapshow.js', 'maplet/message_cn.js'],
                dest: 'dst/gscript.js'
            },

            //任务二：压缩单独的文件。
            buildb: {
                options: {
                    mangle: false,
                    report: "min"//输出压缩率，可选的值有 false(不输出信息)，gzip
                },
                files: {
                    // 简单编辑器
                    'dst/leafedit.js': ['src/Leaflet.Editable.js'],
                    // 地图中全屏然后编辑
                    'dst/fullscreen.js': ['maplet/fullscreenm.js'],
                    // 叠加
                    'dst/overlay.js': ['maplet/overlaym.js'],
                    // 创建地图使用
                    'dst/gsonedit.js': ['maplet/geojson_edit.js'],
                    // 同步
                    'dst/L.Map.Sync.js': ['src/LeafletPlugin/L.Map.Sync.js'],
                    // 滑窗
                    'dst/leaflet-side-by-side.min.js': ['src/LeafletPlugin/leaflet-side-by-side.min.js'],
                    // 百度 Echart中国底图
                    'dst/gson_china.js': ['src/chart/gson_china.js'],
                }
            },
        }
    });

    // 告诉grunt我们将使用插件
    grunt.loadNpmTasks('grunt-contrib-uglify');

    // 告诉grunt当我们在终端中输入grunt时需要做些什么
    grunt.registerTask('default', ['uglify']);
    grunt.registerTask('default2', ['uglify:buildb']);

};
