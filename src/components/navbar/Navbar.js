/*******************************************************************************
 * Copyright (c) 2018 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *******************************************************************************/

import React from 'react';

import { classNames } from '../../common/classnames';

import './Navbar.css';

const NAVBAR__CLASS_NAMES = 'navbar';
const NARBAR_TITLE__CLASS_NAMES = 'navbar-title title-xxl';

/**
 * The Navbar is used to display a navigation bar on top of the user interface.
 * It can contain the title of the application and various actions.
 */
export const Navbar = ({ className, ...props }) => {
  const navClassNames = classNames(NAVBAR__CLASS_NAMES, className);
  return (
    <nav className={navClassNames} {...props}>
      <div className={NARBAR_TITLE__CLASS_NAMES}>Eclipse Sirius</div>
    </nav>
  );
};