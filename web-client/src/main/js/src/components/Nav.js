/**
 * Created by mak on 9/6/16.
 */
import React from 'react';
import { Link } from 'react-router';

export default () => (
  <ul>
    <li><Link to="/">home</Link></li>
    <li><Link to="/not-found">not-found</Link></li>
  </ul>
);
