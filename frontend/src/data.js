import * as Vng from "v-network-graph";

const nodes = {
  node1: { name: "Start Node", shape: 'circle', color: 'green',main: true},
  node2: { name: "End Node" , shape: 'circle', color: 'red',main: true},
  node3: { name: "M1",type: 'machine', shape: 'circle', color: '#FD5200'},
};

const edges = {
    edge1: { source: "node1", target: "node3", color: '#000000' },
    edge2: { source: "node3", target: "node2", color: '#000000' },
};

const layouts = {
  nodes: {
    node1: { x: 300, y: 0 },
    node2: { x: -300, y: 0 },
    node3: { x: 0, y: 0 },
  },
};

const configs = Vng.defineConfigs({
    view: {
        fitView: true,
        // scalingObjects: true,
        layoutHandler: new Vng.GridLayout({ grid: 30 }),
    },
  node: {
    selectable: true,
    draggable: node => !node.main,

    hover:{
        color: node => node.color,
        type: node => node.shape,
    },
    normal:{
        color: node => node.color,
        type: node => node.shape,
    },
    label: {
        fontFamily: "Arial",
        margin: 10,
        fontSize: 20,
    },
    },

    edge: {
    selectable: true,
    hover: {
        width: 5,
        color: edge => edge.color
      },
    normal: {
      width: 5,
      color: edge => edge.color
    },
  },
});

export default {
  nodes,
  edges,
  layouts,
  configs,
};
