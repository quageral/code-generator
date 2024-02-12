import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = '陈廷墀';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'codeNav',
          title: '个人主页',
          href: 'https://github.com/quageral',
          blankTarget: true,
        },

        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> 网站源码
            </>
          ),
          href: 'https://github.com/quageral/code-generator/',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
