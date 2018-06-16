<template>
  <div class="lee-status-card">
    <div class="value">
      <div class="count">{{ count }}</div>
      <span>{{ name }}</span>
    </div>
  </div>
</template>

<script>
export default {
    name: 'statusCard',
    props: ['name', 'count']
}
</script>


<style lang="scss" scoped>
    .lee-status-card {
  display: flex;
  background-color: white;
  border-radius: .4rem;
  min-height: 10rem;
  overflow: hidden;

  .symbol {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40%;
    color: white;
    font-size: 4.5rem;
  }
  .value {
    color: gray;
    display: flex;
    flex-direction: column;
    justify-content: center;
    flex-grow: 1;

    .count {
      font-size: 2.4rem;
    }
    span {
      font-size: 1.2rem;
    }
  }
}

</style>


