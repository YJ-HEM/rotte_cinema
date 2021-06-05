using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Reflection;

namespace rotte_cinema.dao
{
	class Injection
	{
		public static void ParserToObj<T>(List<T> _list, object _obj, string _query)
		{

			DataTable dt = MySqlManager.AdapterRead(_query);

			for (int row = 0; row < dt.Rows.Count; row++)
			{
				// 전달받은 객체의 타입으로 생성.
				object obj = Activator.CreateInstance(_obj.GetType());

				// 전달 받은 객체의 변수 목록을 받아옴.
				FieldInfo[] infoArr = obj.GetType().GetFields(BindingFlags.NonPublic | BindingFlags.Instance);

				// 객체의 변수 목록만큼 반복.
				for (int i = 0; i < infoArr.Length; i++)
				{

					// 데이터 테이블 컬럼의 수만큼 반복하며 객체의 변수명과 동일한 컬럼을 찾음.
					for (int col = 0; col < dt.Columns.Count; col++)
					{
						// <변수명>k__BackingField 이므로 텍스트를 잘라 변수명만 체크함.
						if (infoArr[i].Name.ToUpper().Substring(1).Split(">")[0] == dt.Columns[col].ColumnName.ToUpper())
						{
							// 발견한 데이터를 입력.
							string value = dt.Rows[row][col].ToString();
							FieldInfo info = infoArr[i];
							ConvertObjType(info, obj, value);
							break;
						}
					}

				}
				// 배열에 추가.
				_list.Add((T)obj);
			}
		}

		private static void ConvertObjType(FieldInfo info, object obj, object value)
		{
			// 해당하는 형태의 변수로 변환해서 삽입.
			if (info.FieldType == typeof(System.Int32))
			{
				info.SetValue(obj, Convert.ToInt32(value));
			}
			else if (info.FieldType == typeof(System.Double))
			{
				info.SetValue(obj, Convert.ToString(value));
			}
			else if (info.FieldType == typeof(System.String))
			{
				info.SetValue(obj, Convert.ToString(value));
			}
		}
	}
}
