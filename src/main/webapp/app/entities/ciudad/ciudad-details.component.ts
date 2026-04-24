import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICiudad } from '@/shared/model/ciudad.model';

import CiudadService from './ciudad.service';

export default defineComponent({
  name: 'CiudadDetails',
  setup() {
    const ciudadService = inject('ciudadService', () => new CiudadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const ciudad: Ref<ICiudad> = ref({});

    const retrieveCiudad = async ciudadId => {
      try {
        const res = await ciudadService().find(ciudadId);
        ciudad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.ciudadId) {
      retrieveCiudad(route.params.ciudadId);
    }

    return {
      alertService,
      ciudad,

      previousState,
    };
  },
});
