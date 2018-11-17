// import React from 'react';
// import ReactDOM from 'react-dom';
// import './index.css';
// import App from './App';
// import * as serviceWorker from './serviceWorker';
//
// ReactDOM.render(<App />, document.getElementById('root'));
//
// // If you want your app to work offline and load faster, you can change
// // unregister() to register() below. Note this comes with some pitfalls.
// // Learn more about service workers: http://bit.ly/CRA-PWA
// serviceWorker.unregister();


// function renderApp(newAppState, oldAppState = {}) {
//     if (newAppState === oldAppState) return // 数据没有变化就不渲染了
//     console.log('render app...')
//     renderTitle(newAppState.title, oldAppState.title)
//     renderContent(newAppState.content, oldAppState.content)
// }
//
// function renderTitle(newTitle, oldTitle = {}) {
//     if (newTitle === oldTitle) return // 数据没有变化就不渲染了
//     console.log('render title...')
//     const titleDOM = document.getElementById('title')
//     titleDOM.innerHTML = newTitle.text
//     titleDOM.style.color = newTitle.color
// }
//
// function renderContent(newContent, oldContent = {}) {
//     if (newContent === oldContent) return // 数据没有变化就不渲染了
//     console.log('render content...')
//     const contentDOM = document.getElementById('content')
//     contentDOM.innerHTML = newContent.text
//     contentDOM.style.color = newContent.color
// }
//
// function createStore(stateChanger) {
//     let state = null
//     const listeners = []
//     const subscribe = (listener) => listeners.push(listener)
//     const getState = () => state
//     const dispatch = (action) => {
//         state = stateChanger(state, action)
//         listeners.forEach((listener) => listener())
//     }
//     dispatch({})  // 初始化 state
//     return {getState, dispatch, subscribe}
// }
//
// function reducer(state, action) {
//     if (!state) {
//         return {
//             title: {
//                 text: 'React.js 小书',
//                 color: 'red',
//             },
//             content: {
//                 text: 'React.js 小书内容',
//                 color: 'blue'
//             }
//         }
//     }
//     switch (action.type) {
//         case 'UPDATE_TITLE_TEXT':
//             return { // 构建新的对象并且返回
//                 ...state,
//                 title: {
//                     ...state.title,
//                     text: action.text
//                 }
//             }
//
//         case 'UPDATE_TITLE_COLOR':
//             return { // 构建新的对象并且返回
//                 ...state,
//                 title: {
//                     ...state.title,
//                     color: action.color
//                 }
//             }
//         default:
//             return state
//     }
// }
//
// const store = createStore(reducer)
// let oldState = store.getState() // 缓存旧的 state
// store.subscribe(() => {
//     const newState = store.getState() // 数据可能变化，获取新的 state
//     renderApp(newState, oldState) // 把新旧的 state 传进去渲染
//     oldState = newState // 渲染完以后，新的 newState 变成了旧的 oldState，等待下一次数据变化重新渲染
// })
//
// renderApp(store.getState()) // 首次渲染页面
// store.dispatch({type: 'UPDATE_TITLE_TEXT', text: '《React.js 小书》'}) // 修改标题文本
// store.dispatch({type: 'UPDATE_TITLE_COLOR', color: 'grey'}) // 修改标题颜色


import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types'
import './index.css';
import Header from "./theme/Header";
import Content from "./theme/Content";

class Index extends Component {

    static childContextTypes = {
        store: PropTypes.object
    }

    getChildContext() {
        return {store}
    }

    render() {
        return (
            <div>
                <Header/>
                <Content/>
            </div>
        )
    }
}

function createStore(reducer) {
    let state = null
    const listeners = []
    const subscribe = (listener) => listeners.push(listener)
    const getState = () => state
    const dispatch = (action) => {
        state = reducer(state, action)
        listeners.forEach((listener) => listener())
    }
    dispatch({}) // 初始化 state
    return {getState, dispatch, subscribe}
}

const themeReducer = (state, action) => {
    if (!state) return {
        themeColor: 'red'
    }
    switch (action.type) {
        case 'CHANGE_COLOR':
            return {...state, themeColor: action.themeColor}
        default:
            return state
    }
}

const store = createStore(themeReducer)

ReactDOM.render(<Index/>, document.getElementById('root'));