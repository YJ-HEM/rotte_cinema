using System;
using System.Collections.Generic;
using System.Text;

namespace rotte_cinema.vo
{
    class Showing
    {
        public int showing_index { get; set; }
        public int movie_index { get; set; }
        public string show_starttime { get; set; }
        public string show_date { get; set; }
        public int theater_index { get; set; }
    }
}
