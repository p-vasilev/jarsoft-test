import React from "react";

export function stringArrayToMenuItems(arr: string[]) {
    return arr.map((s: string, i:number) => {
        return {
            key: (i + 1).toString(),
            label: s
        }
    })
}

export function MyLabel(props: {value: string, style?: any}) {
    return <label style={{...{fontSize: "20px", color: "white"}, ...props.style}}>{props.value}</label>
}
