<script setup>
import { ref, computed } from 'vue'
import AppNavbar from '@/components/AppNavbar.vue'
import AppSidebar from '@/components/AppSidebar.vue'

const sidebarRef = ref(null)
const isCollapsed = computed(() => sidebarRef.value?.isCollapsed ?? true)
</script>

<template>
  <div class="app-layout">
    <AppSidebar ref="sidebarRef" />
    <div class="layout-right" :class="{ 'sidebar-collapsed': isCollapsed }">
      <AppNavbar />
      <main class="main-container">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
}

.layout-right {
  margin-left: 180px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: margin-left 0.3s ease;
}

.layout-right.sidebar-collapsed {
  margin-left: 60px;
}

.main-container {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  overflow-y: auto;
}
</style>
