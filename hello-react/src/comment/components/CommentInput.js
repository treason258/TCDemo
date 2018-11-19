import React from 'react';
import BaseComponent from '../BaseComponent.js'
import PropTypes from 'prop-types'
import '../Comment.css';

class CommentInput extends BaseComponent {

    static propTypes = {
        username: PropTypes.any,
        onSubmit: PropTypes.func,
        onUserNameInputBlur: PropTypes.func
    }

    static defaultProps = {
        username: ''
    }

    constructor(props) {
        super(props)
        this.state = {
            username: props.username, // 从 props 上取 username 字段
            content: ''
        }
    }

    componentWillMount() {
        // this._loadUsername()
    }

    componentDidMount() {
        this.textarea.focus()
    }

    // _saveUsername(username) {
    //     localStorage.setItem('username', username)
    // }

    // _loadUsername() {
    //     const username = localStorage.getItem('username')
    //     if (username) {
    //         this.setState({username})
    //     }
    // }

    handleUsernameChange(event) {
        this.setState({
            username: event.target.value
        })
    }

    handleContentChange(event) {
        this.setState({
            content: event.target.value
        })
    }

    handleUsernameBlur(event) {
        // this._saveUsername(event.target.value)
        if (this.props.onUserNameInputBlur) {
            this.props.onUserNameInputBlur(event.target.value)
        }
    }

    handleSubmit() {
        if (this.props.onSubmit) {
            // const {username, content} = this.state
            // this.props.onSubmit({username, content})
            this.props.onSubmit({
                username: this.state.username,
                content: this.state.content,
                createdTime: +new Date()
            })
        }
        this.setState({content: ''})
    }

    render() {
        return (
            <div className='comment-input'>
                <div className='comment-field'>
					<span className='comment-field-name'>
						用户名：
					</span>
                    <div className='comment-field-input'>
                        <input
                            value={this.state.username}
                            onBlur={this.handleUsernameBlur.bind(this)}
                            onChange={this.handleUsernameChange.bind(this)}
                        />
                    </div>
                </div>
                <div className='comment-field'>
					<span className='comment-field-name'>
						评论内容：
                    </span>
                    <div className='comment-field-input'>
                        <textarea
                            ref={(textarea) => this.textarea = textarea}
                            value={this.state.content}
                            onChange={this.handleContentChange.bind(this)}
                        />
                    </div>
                </div>
                <div className='comment-field-button'>
                    <button onClick={this.handleSubmit.bind(this)}>
                        发布
                    </button>
                </div>
            </div>
        )
    }
}

export default CommentInput;