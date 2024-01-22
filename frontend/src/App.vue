<template>
  <div class="all">
    <v-network-graph v-model:selected-nodes="selectedNodes" v-model:selected-edges="selectedEdges"
      :selected-edges="selectedEdges" :nodes="nodes" :edges="edges" :paths="paths" :layouts="data.layouts"
      :configs="configs" />
    <div class="panel" v-bind:style="{ left: `${x}px`, top: `${y}px` }" @mousedown="startDrag" @mousemove="dragging"
      @mouseup="stopDrag" @mouseleave="stopDrag">
      <button @click="AddMachine" class="addm">🏭</button>
      <button @click="AddQueue" class="addq">📦</button>
      <button @click="AddEdge" class="adde">🔗</button>
      <button v-if="!running" @click="Run" class="run">▶️</button>
      <button v-if="running" @click="Pause" class="pause">⏸</button>
      <button @click="Replay" class="replay">🔄</button>
      <button @click="remove" class="delete">❌</button>
      <button @click="clear" class="clear">🗑️</button>


    </div>
  </div>
</template>

<script>

export default {
  name: 'App',

  data() {
    return {
      x: window.innerWidth / 3,
      y: 3 * window.innerHeight / 4,
      startX: 0,
      startY: 0,
      isDragging: false,
      isPopupVisible: false,
      running: false,
      nodes,
      edges,
      selectedNodes: sn,
      selectedEdges: se,
      nextMachineIndex: 2,
      nextQueueIndex: 1,
      nextNodeIndex,
      nextEdgeIndex,
      configs,
      data,
      paths,
      ref
    };
  },
  mounted() {
    console.log("mounted");
    this.update();
  },
  methods: {
    Run() {
      const visited = {};
      this.paths = this.findAllPaths('node1', 'node2', visited, [], this.edges);
      console.log(this.paths);
      this.configs.path.visible = !this.configs.path.visible;
      this.running = true;

      // create fetch post request with payloads in following format
      // {
      //   "nodes": {
      // "node1":{ "name": "Start Node", "shape": "circle", "color": "green","main": true,"type":"queue","x":0,"y":0},
      // "node2":{ "name": "End Node", "shape": "circle", "color": "green","main": true,"type":"queue","x":0,"y":0},
      // "node3":{ "name": "Q1", "shape": "circle", "color": "green","main": true,"type":"queue","x":0,"y":0},
      // "node4":{ "name": "M1", "shape": "square", "color": "red","main": true,"type":"machine","x":0,"y":0},
      // }
      //   "edges": {
      // "edge1":{ "source": "node1", "target": "node3", "color": "black"},
      // "edge2":{ "source": "node3", "target": "node4", "color": "black"},
      // "edge3":{ "source": "node4", "target": "node2", "color": "black"},
      // }
      // }
      let nodes = {};
      for (let nodeI in this.nodes) {
        let node = this.nodes[nodeI];
        console.log(nodeI, node);
        nodes[nodeI] = { "name": node.name.split(":")[0], "shape": node.shape, "color": node.color, "main": node.main, "type": node.type, "x": 0, "y": 0 };
      }
      
      let edges = {};
      for (let edgeI in this.edges) {
        let edge = this.edges[edgeI];
        console.log(edgeI, edge);
        edges[edgeI] = { "source": edge.source, "target": edge.target, "color": edge.color };
      }

      let data = {
        "nodes": nodes,
        "edges": edges
      }

      console.log(data);

      fetch('http://localhost:8080/data', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
      })
        .then(response => response.text())
        .then(data => {
          console.log('Success:', data);
        })

    },
    findAllPaths(current, target, visited, path, edges) {
      visited[current] = true;
      let allPaths = [];

      for (const edgeId in edges) {
        const edge = edges[edgeId];
        if (edge.source === current && !visited[edge.target]) {
          path.push(edgeId);
          if (edge.target === target) {
            allPaths.push({ edges: [...path] });
          } else {
            allPaths = allPaths.concat(this.findAllPaths(edge.target, target, visited, path, edges));
          }
          path.pop();
        }
      }

      visited[current] = false;
      return allPaths;
    },
    Pause() {
      this.configs.path.visible = !this.configs.path.visible;
      this.running = false;
    },
    Replay() {

    },
    remove() {
      if (this.running) {
        alert('Please pause the simulation first.');
        return;
      }

      if (this.selectedNodes.length === 0 && this.selectedEdges.length === 0) {
        alert('Please select a node or an edge to delete.');
        return;
      }
      for (const nodeId of this.selectedNodes) {
        if (!this.nodes[nodeId].main) {
          if (this.nodes[nodeId].type === 'machine') {
            this.nextMachineIndex--;
          } else if (this.nodes[nodeId].type === 'queue') {
            this.nextQueueIndex--;
          }
          delete this.nodes[nodeId];
        }
      }
      for (const edgeId of this.selectedEdges) {
        delete this.edges[edgeId];
      }
    },
    AddMachine() {

      if (this.running) {
        alert('Please pause the simulation first.');
        return;
      }

      const nodeId = `node${nextNodeIndex.value}`;
      const name = `M${this.nextMachineIndex}`;
      nodes[nodeId] = { name, type: 'machine', shape: 'circle', color: '#386641' };
      this.nextMachineIndex++;
      this.nextNodeIndex++;
    },
    AddQueue() {

      if (this.running) {
        alert('Please pause the simulation first.');
        return;
      }
      const nodeId = `node${nextNodeIndex.value}`;
      const name = `Q${this.nextQueueIndex}`;
      nodes[nodeId] = { name, type: 'queue', shape: 'rect', color: '#FFDF64' };
      this.nextQueueIndex++;
      this.nextNodeIndex++;
    },
    AddEdge() {

      if (this.running) {
        alert('Please pause the simulation before deleting any node or edge.');
        return;
      }
      if (this.selectedNodes.length !== 2) {
        alert('Please select two nodes to connect.');
        return;
      }
      const [source, target] = this.selectedNodes;
      if (this.nodes[source].type !== this.nodes[target].type) {

        // Check if the source node is a machine and if it's already a source of any other edge
        if (this.nodes[source].type === 'machine' && Object.values(this.edges).some(edge => edge.source === source)) {
          alert('This machine is already connected to a queue.');
          return;
        }
        const edgeId = `edge${this.nextEdgeIndex}`;
        this.edges[edgeId] = { source, target, color: '#000000' };
        this.nextEdgeIndex++;
      }
      else {
        alert('Please select two nodes of different types to connect.');
      }
    },
    clear() {
      window.location.reload();
    },


    startDrag(event) {
      this.isDragging = true;
      this.startX = event.clientX - this.x;
      this.startY = event.clientY - this.y;
    },
    dragging(event) {
      if (this.isDragging && !this.isPopupVisible) {
        this.x = event.clientX - this.startX;
        this.y = event.clientY - this.startY;
      }
    },
    stopDrag() {
      this.isDragging = false;
    },
    update() {
      // register function to call every 100ms that fetch data from backend
      // and update the nodes and edges

      setInterval(() => {
        if (this.running) {
          console.log("fetchhh");

          fetch('http://localhost:8080/data', {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          })
            .then(response => response.json())
            .then(data =>{
              console.log(data);
              for(let node in data){
                for(let node2 in this.nodes){
                  console.log(this.nodes[node2].name, node);
                  if(this.nodes[node2].name=== node && this.nodes[node2].type === "machine"){
                    this.nodes[node2].color = data[node];
                  }
                  const qn = this.nodes[node2].name.split(":")[0];
                  if(qn=== node && this.nodes[node2].type === "queue"){
                    this.nodes[node2].name = qn + ": " + data[node];
                  }
                }
              }
            })
            .catch((error) => {
              console.error('Error:', error);
            });
        }
      }, 100);
    }
  }
}

import { reactive, ref } from "vue";
import data from "./data.js";

const nodes = reactive({ ...data.nodes });
const edges = reactive({ ...data.edges });
const configs = reactive({ ...data.configs });
const paths = reactive({ ...data.paths });
const nextNodeIndex = ref(Object.keys(nodes).length + 1);
const nextEdgeIndex = ref(Object.keys(edges).length + 1);

// const nextMIndex = ref(Object.values(nodes).filter(node => node.type === 'machine').length + 1);
// const nextQIndex = ref(Object.values(nodes).filter(node => node.type === 'queue').length + 1);;

const sn = ref([]);
const se = ref([]);

</script>

<style scoped src="./App.css" lang="css"></style>