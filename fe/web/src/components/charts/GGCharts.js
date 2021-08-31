import React from 'react'

import { Chart } from "react-google-charts";

const data = [
    ['x', 'vn', 'fr','am'],
    [0, 0, 0,0],
    [1, 10, 5,0],
    [2, 23, 15,0],
    [3, 17, 9,6],
    [4, 18, 10,6],
    [5, 9, 5,5],
    [6, 11, 3,3],
    [9, 27, 19,6],
]

const options = {
    title: 'Chart',
    hAxis: {
        title: 'Time',
        titleTextStyle: { color: '#752342' } 
    },
    vAxis: {
        title: 'Popularity',
    },
    // series: {
    //     1: { curveType: 'function' },
    // },
    seriesType: 'line',
    series: { 2: { type: 'bar' } },
    // bubble: { textStyle: { fontSize: 11 } }, for bubble Chart
}


const chartEvents = [
    {
        eventName: 'select',
        callback: ({ chartWrapper }) => {
          const chart = chartWrapper.getChart()
          const selection = chart.getSelection()
          if (selection.length === 1) {
            const [selectedItem] = selection
            const dataTable = chartWrapper.getDataTable()
            const { row, column } = selectedItem
            alert(
              'You selected : ' +
                JSON.stringify({
                  row,
                  column,
                  value: dataTable.getValue(row, column),
                }),
              null,
              2,
            )
          }
          console.log(selection)
          },
    },
]

const GGCharts = () => {
    return (
        <div>
            <Chart
                width={'100%'}
                height={'400px'}
                chartType="LineChart"
                loader={<div>Loading Chart</div>}
                data={data}
                options={options}
                chartEvents={chartEvents}
            />

        </div>
    )
}

export default GGCharts
