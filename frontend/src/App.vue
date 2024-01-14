<template>
  <div class="all">
    <v-network-graph
    :nodes="nodes"
    :edges="edges"
    :layouts="layouts"
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
      <button @click="Delete">❌</button>
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
      edges
    };
  },
  methods: {
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

const nodes = {
    node1: { name: "Node 1" },
    node2: { name: "Node 2" },
    node3: { name: "Node 3" },
    node4: { name: "Node 4" },
  }
const edges = {
  edge1: { source: "node1", target: "node2" },
  edge2: { source: "node2", target: "node3" },
  edge3: { source: "node3", target: "node4" },
}
</script>

<style scoped src="./App.css" lang="css"></style>