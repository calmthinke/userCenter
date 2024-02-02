import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';

// @ts-ignore
import React, {useRef} from 'react';
import {searchUsers} from "@/services/ant-design-pro/api";
import {Image} from "antd";

export default () => {
  const actionRef = useRef<ActionType>();

  const columns: ProColumns<API.CurrentUser>[] = [
    {
      title: '星球编号',
      dataIndex: 'planetCode',
      valueType: 'indexBorder',
      width: 48,

    },
    {
      title: '用户名',
      key: 'showTime1',
      dataIndex: 'username',
    },

    {
      title: '用户账户',
      key: 'showTime3',
      dataIndex: 'userAccount',
    },
    {
      title: '头像',
      key: 'showTime4',
      dataIndex: 'avatarUrl',
      render:(_,record)=>(
       <Image src={record.avatarUrl} width={100}></Image>
      ),
      valueType:'avatar'
    },
    {
      title: '性别',
      key: 'showTime5',
      dataIndex: 'gender',
      valueType: 'select',
      valueEnum: {
        1: { text: '男' },
        2: {
          text: '女',

        },
      },
    },
    {
      title: '电话',
      dataIndex: 'userPhone',
    },
    {
      title: '邮箱',
      key: 'showTime7',
      dataIndex: 'email',
    },
    {
      title: '角色',
      key: 'showTime8',
      dataIndex: 'userRole',
      valueType: 'select',
      valueEnum: {
        0: { text: '普通用户', status: 'Default' },
        1: {
          text: '管理员',
          status: 'Success',
        },
      },
    },
    {
      title: '状态',
      key: 'showTime9',
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: {
        0: { text: '正常', status: 'Success' },
        1: {
          text: '封号',
          status: 'Default',
        },
      },
    },
    {
      title: '创建时间',
      key: 'showTime10',
      // dataIndex: 'creatTime',
      valueType:'date'
    },
    // {
    //   title: '操作',
    //   valueType: 'option',
    //   key: 'option',
    //   render: (text, record, _, action) => [<a>详情</a>],
    // },
  ];


  //获取table数据 ------ 封装api接口
  const getTableData = async (param: any) => {
    const userList=await searchUsers();
    // const { data, success } = await fetchCompanyList(params);
    return {
      data: userList,
      success: true,
    };
  };
  return (
    <>
      <ProTable<API.CurrentUser>
        columns={columns}
        actionRef={actionRef}
        // @ts-ignore
        request={getTableData}
        headerTitle="结果查询"
        search={{ labelWidth: 'auto' }}
      />
    </>
  );
};
