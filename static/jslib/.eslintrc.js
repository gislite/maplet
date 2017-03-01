module.exports = {
    "env": {
      "browser": true,
      "commonjs": true,
      "es6": true,
      "jquery": true
    },

    "globals": {
        "_id": true,
      },
    "extends": "eslint:recommended",
    "rules": {
        // "indent": [
        //     "error",
        //     "space"
        // ],
        "linebreak-style": [
            "error",
            "unix"
        ],
        "quotes": [
            "error",
            "double"
        ],
        "semi": [
            "error",
            "always"
        ]
    }
};
