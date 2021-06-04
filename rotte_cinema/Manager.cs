using System;
using System.Collections.Generic;
using System.Text;
using MySql.Data.MySqlClient;
using System.Data;

namespace rotte_cinema
{
    class Manager
    {
        private static MySqlConnection connection = new MySqlConnection("Server = db.kumas.dev; port=3306; database=Cinema; uid=user; pwd=0000");

       static public int Input(string sql) {
            connection.Open();
            MySqlCommand input = new MySqlCommand(sql, connection);
            connection.Close();
            return input.ExecuteNonQuery();
        }

        static public int Delete(string sql)
        {
            connection.Open();
            MySqlCommand delete = new MySqlCommand(sql, connection);
            connection.Close();
            return delete.ExecuteNonQuery();
        }


        static public int Insert(string sql)
        {
            connection.Open();
            MySqlCommand insert = new MySqlCommand(sql, connection);
            connection.Close();
            return insert.ExecuteNonQuery();
        }


        static public int Update(string sql)
        {
            connection.Open();
            MySqlCommand update = new MySqlCommand(sql, connection);
            connection.Close();
            return update.ExecuteNonQuery();
        }

        static public int Select(string sql)
        {
            connection.Open();
            MySqlCommand select = new MySqlCommand(sql, connection);
            connection.Close();
            return select.ExecuteNonQuery();
        }

        static public DataTable AdapterRead(string sql) {
            DataTable dt = new DataTable();
            connection.Open();
            MySqlDataAdapter adapter = new MySqlDataAdapter(sql, connection);
            adapter.Fill(dt);
            connection.Close();
            return dt;
        }


    }

}
