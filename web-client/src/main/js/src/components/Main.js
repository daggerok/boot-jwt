/**
 * spring-boot + jquery + CORS example
 */
import React from 'react';
import Nav from "./Nav";
import $ from 'jquery';

export default class Main extends React.Component {
  constructor() {
    super();

    this.getAccessToken = this.getAccessToken.bind(this);
    this.fetchItmes = this.fetchItmes.bind(this);

    this.username = 'web_app';
    this.password = '';

    this.state = {
      headers: {},
      items: []
    };
  }

  getAccessToken() {

    const credentials = btoa(`${this.username}:${this.password}`);
    const data = {
      'grant_type': 'password',
      'username': 'rest_reader_username',
      'password': 'rest_reader_password'
    };

    $.post({
      url: 'http://localhost:9999/uaa/oauth/token',
      type: 'POST',
      data,
      contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
      beforeSend: (xhr) => xhr.setRequestHeader('Authorization', `Basic ${credentials}`),
      success: (data) => {
        this.setState({
          headers: { Authorization: `Bearer ${data['access_token']}` }
        });
      },
      error: (xhr) => console.log('err', xhr)
    });
  }

  fetchItmes() {
    this.retry = false;

    $.ajax({
      url: 'http://localhost:9000/api/items',
      dataType: 'json',
      headers: this.state.headers,
      error: (jqXHR, textStatus, errorThrown) => {
        console.log('err', textStatus, errorThrown);
        if (jqXHR.statusCode() === 401 && !this.retry) {
          this.retry = !this.retry;
          this.tokenRequest = this.getAccessToken();
          this.tokenRequest.then(() => this.itemsRequest = this.fetchItmes)
        }
      },
      success: (data) => {
        this.setState({
          items: [
            ...data['_embedded']['items']
          ]
        })
      },
    });
  }

  componentDidMount() {
    this.tokenRequest = this.getAccessToken();
  }

  componentWillUnmount() {
    if (this.tokenRequest) this.tokenRequest.abort();
    if (this.itemsRequest) this.itemsRequest.abort();
  }

  render() {
    return (
        <div>
          <span>hi</span>
          <ul>
            {this.state.items.map(item => <li key={item.id}>{item.content}</li>)}
          </ul>
          <button class="btn btn-success" onClick={() => {
            this.tokenRequest = this.getAccessToken();
          }}>fetch access token</button>
          <button class="btn btn-primary"
                  onClick={this.fetchItmes}>fetch items</button>
          <Nav/>
        </div>
    );
  }
}
