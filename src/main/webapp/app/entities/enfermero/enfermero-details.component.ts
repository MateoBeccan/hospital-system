import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IEnfermero } from '@/shared/model/enfermero.model';

import EnfermeroService from './enfermero.service';

export default defineComponent({
  name: 'EnfermeroDetails',
  setup() {
    const enfermeroService = inject('enfermeroService', () => new EnfermeroService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const enfermero: Ref<IEnfermero> = ref({});

    const retrieveEnfermero = async enfermeroId => {
      try {
        const res = await enfermeroService().find(enfermeroId);
        enfermero.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.enfermeroId) {
      retrieveEnfermero(route.params.enfermeroId);
    }

    return {
      alertService,
      enfermero,

      previousState,
    };
  },
});
