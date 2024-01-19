<template>
  <div class="all">
    <v-network-graph
      v-model:selected-nodes="selectedNodes"
      v-model:selected-edges="selectedEdges"
      :selected-edges="selectedEdges"
      :nodes="nodes"
      :edges="edges"
      :paths="paths"
      :layouts="data.layouts"
      :configs="configs"
    />
    <div
      class="panel"
      v-bind:style="{ left: `${x}px`, top: `${y}px` }"
      @mousedown="startDrag"
      @mousemove="dragging"
      @mouseup="stopDrag"
      @mouseleave="stopDrag"
    >
      <button @click="AddMachine" class="addm">🏭</button>
      <button @click="AddQueue" class="addq">📦</button>
      <button @click="AddEdge" class="adde">🔗</button>
      <button @click="Run" class="run">▶️</button>
       <!-- <button @click="Pause" class="pause">⏸</button>-->
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
      nodes,
      edges,
      selectedNodes:sn,
      selectedEdges:se,
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
  methods: {
    Run() {
      this.configs.path.visible = !this.configs.path.visible;
    },
    remove() {
      for (const nodeId of this.selectedNodes) {
        if(!this.nodes[nodeId].main) {
          if (this.nodes[nodeId].type === 'machine') {
            this.nextMachineIndex--;
          } else if (this.nodes[nodeId].type === 'queue') {
            this.nextQueueIndex--;
          }
          this.nextNodeIndex--;
          delete this.nodes[nodeId];
        }
      }
      for (const edgeId of this.selectedEdges) {
        delete this.edges[edgeId];
      }
    },
    AddMachine() {
      const nodeId = `node${nextNodeIndex.value}`;
      const name = `M${this.nextMachineIndex}`;
      nodes[nodeId] = { name, type: 'machine', shape: 'circle', color: '#386641' };
      this.nextMachineIndex++;
      this.nextNodeIndex++;
    },
    AddQueue() {
      const nodeId = `node${nextNodeIndex.value}`;
      const name = `Q${this.nextQueueIndex}`;
      nodes[nodeId] = { name, type: 'queue', shape: 'rect', color: '#FFDF64'};
      this.nextQueueIndex++;
      this.nextNodeIndex++;
    },
    AddEdge() {
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
        this.edges[edgeId] = { source, target , color: '#000000'};
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
    }
  }
}

import { reactive, ref } from "vue";
import data from "./data.js";

const nodes = reactive({ ...data.nodes });
const edges = reactive({ ...data.edges });
const configs = reactive({ ...data.configs});
const paths = reactive({ ...data.paths});
const nextNodeIndex = ref(Object.keys(nodes).length + 1);
const nextEdgeIndex = ref(Object.keys(edges).length + 1);

// const nextMIndex = ref(Object.values(nodes).filter(node => node.type === 'machine').length + 1);
// const nextQIndex = ref(Object.values(nodes).filter(node => node.type === 'queue').length + 1);;

const sn = ref([]);
const se = ref([]);

</script>

<style scoped src="./App.css" lang="css"></style>