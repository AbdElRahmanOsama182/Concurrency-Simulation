<template>
  <div class="all">
    <v-network-graph
      v-model:selected-nodes="selectedNodes"
      v-model:selected-edges="selectedEdges"
      :selected-edges="selectedEdges"
      :nodes="nodes"
      :edges="edges"
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
      <button @click="AddMachine">🏭</button>
      <button @click="AddQueue">📦</button>
      <button @click="AddEdge">🔗</button>
      <button @click="Run">▶️</button>
       <!-- <button @click="Pause">⏸</button>-->
      <button @click="remove">❌</button>
      <button @click="Reset">🔄</button>
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
      ref
    };
  },
  methods: {
    remove() {
      for (const nodeId of this.selectedNodes) {
        if(!this.nodes[nodeId].main) {
          delete this.nodes[nodeId];
          if (this.nodes[nodeId].type === 'machine') {
            this.nextMachineIndex--;
          } else if (this.nodes[nodeId].type === 'queue') {
            this.nextQueueIndex--;
          }
        }
      }
      for (const edgeId of this.selectedEdges) {
        delete this.edges[edgeId];
      }
    },
    AddMachine() {
      const nodeId = `node${nextNodeIndex.value}`;
      const name = `M${nextMachineIndex.value}`;
      nodes[nodeId] = { name, type: 'machine', shape: 'circle', color: '#FD5200' };
      this.nextMachineIndex++;
      this.nextNodeIndex++;
    },
    AddQueue() {
      const nodeId = `node${nextNodeIndex.value}`;
      const name = `Q${nextQueueIndex.value}`;
      nodes[nodeId] = { name, type: 'queue', shape: 'rect', color: '#00FFE7'};
      this.nextQueueIndex++;
      this.nextNodeIndex++;
    },
    AddEdge() {
      if (sn.value.length !== 2) return;
      const [source, target] = sn.value;
      if (nodes[source].name[0] !== nodes[target].name[0]) {
        const edgeId = `edge${nextEdgeIndex.value}`;
        edges[edgeId] = { source, target , color: '#000000'};
        nextEdgeIndex.value++;
      }
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
const configs = reactive({...data.configs})
const nextNodeIndex = ref(Object.keys(nodes).length + 1);
const nextEdgeIndex = ref(Object.keys(edges).length + 1);
const nextMachineIndex = ref(2);
const nextQueueIndex = ref(1);

const sn = ref([]);
const se = ref([]);

</script>

<style scoped src="./App.css" lang="css"></style>