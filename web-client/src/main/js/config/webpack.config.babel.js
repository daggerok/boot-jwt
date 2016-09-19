/**
 * Created by mak on 9/6/16.
 */
import path from 'path';
import HtmlWebpackPlugin from 'html-webpack-plugin';
import ExtractPlugin from 'extract-text-webpack-plugin';
import autoprefixer from 'autoprefixer';

const include   = [path.resolve(process.cwd(), './src')];
const assetsInclude   = [path.resolve(process.cwd(), './src/assets')];
const config = {
  entry: {
    app: ['./src/main.js']
  },
  output: {
    path: '../resources/static',
    filename: '/[name].js'
  },
  module: {
    preLoaders: [{
      test: /\.js$/,
      exclude: /(node_modules|bower_components)/,
      loader: 'source-map-loader'
    }],
    loaders: [{
      test: /\.js$/,
      include,
      loader: 'babel',
      query: {
        presets: ['stage-0', 'es2015', 'react'],
        plugins: ['react-html-attrs', 'add-module-exports', 'transform-class-properties']
      }
    }, {
      test: /\.css/,
      include: assetsInclude,
      // // fix: Module build failed: ReferenceError: window is not defined
      // loader: ExtractPlugin.extract('style!css')
      loader: ExtractPlugin.extract('style', 'css!postcss')
    }, {
      test: /\.less$/,
      include: assetsInclude,
      loader: ExtractPlugin.extract('style','css!postcss!less')
    }, {
      test: /\.styl/,
      include: assetsInclude,
      loader: ExtractPlugin.extract('style','css!postcss!stylus')
    }, {
      test: /\.hbs$/,
      include,
      loader: 'handlebars'
    }]
  },
  resolve: {
    extensions: ['', '.js'],
    modulesDirectories: ['node_modules']
  },
  plugins: [
    new ExtractPlugin('/[name].css', { allChunks: true }),
    // new HtmlWebpackPlugin({ template: './src/assets/index.hbs' })
  ],
  postcss: [ autoprefixer({ browsers: ['last 2 versions'] }) ],
  devtool: '#cheap-module-inline-source-map',
  devServer: {
    historyApiFallback: true,
    inline:   true,
    progress: true
  },
  externals: {
    // Use external version of React
    "react": "React",
    "react-dom": "ReactDOM",
    "jquery": "$",
    "bootstrap": "bootstrap/dist/css/bootstrap.css"
  },
  node: {
    net: "empty",
    tls: "empty"
  }
};

export default config;
