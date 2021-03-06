/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
import { Text } from 'core/text/Text';
import PropTypes from 'prop-types';
import React from 'react';
import styles from './Title.module.css';

const propTypes = {
  label: PropTypes.string.isRequired,
};

export const Title = ({ label }) => {
  return (
    <div className={styles.titleContainer}>
      <Text className={styles.title}>{label}</Text>
    </div>
  );
};
Title.propTypes = propTypes;
