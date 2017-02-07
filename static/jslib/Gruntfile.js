// 包装函数
module.exports = function (grunt) {

    // 任务配置,所有插件的配置信息
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

 //        concat: {
 // options: {},
 //            css: {
 //
 //                src: ['src/gscript.js', 'src/message_cn.js'],
 //                dest: 'build/a.js'
 //            }
 //
 //        },


        // uglify插件的配置信息
        uglify: {
            options: {mangle: false,},
            build: {
                src: ['src/gscript.js', 'maplet/mapshow.js', 'maplet/message_cn.js'],
                dest: 'dst/gscript.js'
            },

            buildb:{//任务二：压缩b.js，输出压缩信息
                options: {
                    mangle: false,
                    report: "min"//输出压缩率，可选的值有 false(不输出信息)，gzip
                },
                files: {
                    'dst/leafedit.js': ['src/Leaflet.Editable.js']
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
