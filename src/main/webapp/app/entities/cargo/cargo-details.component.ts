import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICargo } from '@/shared/model/cargo.model';

import CargoService from './cargo.service';

export default defineComponent({
  name: 'CargoDetails',
  setup() {
    const cargoService = inject('cargoService', () => new CargoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cargo: Ref<ICargo> = ref({});

    const retrieveCargo = async cargoId => {
      try {
        const res = await cargoService().find(cargoId);
        cargo.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoId) {
      retrieveCargo(route.params.cargoId);
    }

    return {
      alertService,
      cargo,

      previousState,
    };
  },
});
